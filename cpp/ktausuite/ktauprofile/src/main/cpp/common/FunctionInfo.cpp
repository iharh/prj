/****************************************************************************
**			TAU Portable Profiling Package			   **
**			http://www.cs.uoregon.edu/research/tau	           **
*****************************************************************************
**    Copyright 1997  						   	   **
**    Department of Computer and Information Science, University of Oregon **
**    Advanced Computing Laboratory, Los Alamos National Laboratory        **
****************************************************************************/
/***************************************************************************
**	File 		: FunctionInfo.cpp				  **
**	Description 	: TAU Profiling Package				  **
*	Contact		: tau-team@cs.uoregon.edu 		 	  **
**	Documentation	: See http://www.cs.uoregon.edu/research/tau      **
***************************************************************************/

//////////////////////////////////////////////////////////////////////
// Include Files 
//////////////////////////////////////////////////////////////////////

#include "Profile/Profiler.h"


#ifdef TAU_DOT_H_LESS_HEADERS
#include <iostream>
using namespace std;
#else /* TAU_DOT_H_LESS_HEADERS */
#include <iostream.h>
#endif /* TAU_DOT_H_LESS_HEADERS */

#include <stdio.h> 
#include <fcntl.h>
#include <time.h>
#include <stdlib.h>

#if (!defined(TAU_WINDOWS))
 #include <unistd.h>
#else
  #include <vector>
#endif //TAU_WINDOWS

#ifdef TAU_VAMPIRTRACE 
#include <Profile/TauVampirTrace.h>
#else /* TAU_VAMPIRTRACE */
#ifdef TAU_EPILOG
#include "elg_trc.h"
#endif /* TAU_EPILOG */
#endif /* TAU_VAMPIRTRACE */

#ifdef TAU_SILC
#include <Profile/TauSilc.h>
#endif

#include <Profile/TauTrace.h>
#include <Profile/TauInit.h>

//////////////////////////////////////////////////////////////////////
// The purpose of this subclass of vector is to give us a chance to execute
// some code when TheFunctionDB is destroyed.  For Dyninst, this is necessary
// when running with fortran programs
//////////////////////////////////////////////////////////////////////
class FIvector : public vector<FunctionInfo*> {
public: 
  ~FIvector() {
    Tau_destructor_trigger();
  }
};

//////////////////////////////////////////////////////////////////////
// Instead of using a global var., use static inside a function  to
// ensure that non-local static variables are initialised before being
// used (Ref: Scott Meyers, Item 47 Eff. C++).
//////////////////////////////////////////////////////////////////////
vector<FunctionInfo*>& TheFunctionDB(void)
{ // FunctionDB contains pointers to each FunctionInfo static object

  // we now use the above FIvector, which subclasses vector
  //static vector<FunctionInfo*> FunctionDB;
  static FIvector FunctionDB;

  static int flag = 1;
  if (flag) {
    flag = 0;
    InitializeTAU();
  }

  return FunctionDB;
}

//////////////////////////////////////////////////////////////////////
// It is not safe to call Profiler::StoreData() after 
// FunctionInfo::~FunctionInfo has been called as names are null
//////////////////////////////////////////////////////////////////////
int& TheSafeToDumpData()
{ 
  static int SafeToDumpData=1;

  return SafeToDumpData;
}

//////////////////////////////////////////////////////////////////////
// Set when uning Dyninst
//////////////////////////////////////////////////////////////////////
int& TheUsingDyninst()
{ 
  static int UsingDyninst=0;
  return UsingDyninst;
}

//////////////////////////////////////////////////////////////////////
// Set when uning Compiler Instrumentation
//////////////////////////////////////////////////////////////////////
int& TheUsingCompInst()
{ 
  static int UsingCompInst=0;
  return UsingCompInst;
}

#ifdef TAU_VAMPIRTRACE
//////////////////////////////////////////////////////////////////////
// Initialize VampirTrace Tracing package
//////////////////////////////////////////////////////////////////////
int TauInitVampirTrace(void)
{
  DEBUGPROFMSG("Calling vt_open"<<endl;);
  vt_open();
  return 1;
}
#endif /* TAU_VAMPIRTRACE */

#ifdef TAU_EPILOG 
//////////////////////////////////////////////////////////////////////
// Initialize EPILOG Tracing package
//////////////////////////////////////////////////////////////////////
int TauInitEpilog(void) {
  DEBUGPROFMSG("Calling esd_open"<<endl;);
  esd_open();
  return 1;
}
#endif /* TAU_EPILOG */

//////////////////////////////////////////////////////////////////////
// Member Function Definitions For class FunctionInfo
//////////////////////////////////////////////////////////////////////


static char *strip_tau_group(const char *ProfileGroupName) {
  char *source = strdup(ProfileGroupName);
  const char *find = "TAU_GROUP_";
  char *ptr;

  while (ptr = strstr(source,find)) {
    char *endptr = ptr+strlen(find);
    while (*endptr != '\0') {
      *ptr++ = *endptr++;
    }
  }
  return source;
}

//////////////////////////////////////////////////////////////////////
// FunctionInfoInit is called by all four forms of FunctionInfo ctor
//////////////////////////////////////////////////////////////////////
void FunctionInfo::FunctionInfoInit(TauGroup_t ProfileGroup, 
	const char *ProfileGroupName, bool InitData, int tid) {

  /* Make sure TAU is initialized */
  static int flag = 1;
  if (flag) {
    flag = 0;
    InitializeTAU();
  }

  //Need to keep track of all the groups this function is a member of.
  AllGroups = strip_tau_group(ProfileGroupName);
  GroupName = strdup(RtsLayer::PrimaryGroup(AllGroups).c_str());

  // Since FunctionInfo constructor is called once for each function (static)
  // we know that it couldn't be already on the call stack.
  RtsLayer::LockDB();
  // Use LockDB to avoid a possible race condition.
  
  //Add function name to the name list.
  TauProfiler_theFunctionList(NULL, NULL, true, (const char *)GetName());
  
  if (InitData) {
    for (int i=0; i < TAU_MAX_THREADS; i++) {
      NumCalls[i] = 0;
      SetAlreadyOnStack(false, i);
      NumSubrs[i] = 0;
      for(int j=0;j<Tau_Global_numCounters;j++){
	ExclTime[i][j] = 0;
	InclTime[i][j] = 0;
      } 
    }
  }
  
  // Make this a ptr to a list so that ~FunctionInfo doesn't destroy it.
  
  for (int i=0; i<TAU_MAX_THREADS; i++) {
    MyProfileGroup_[i] = ProfileGroup;
  }
  // While accessing the global function database, lock it to ensure
  // an atomic operation in the push_back and size() operations. 
  // Important in the presence of concurrent threads.
  TheFunctionDB().push_back(this);
  FunctionId = RtsLayer::GenerateUniqueId();

#ifdef TAU_VAMPIRTRACE
  static int tau_vt_init=TauInitVampirTrace();
  string tau_vt_name(string(Name)+" "+string(Type));
  FunctionId = TAU_VT_DEF_REGION(tau_vt_name.c_str(), VT_NO_ID, VT_NO_LNO,
			     VT_NO_LNO, GroupName, VT_FUNCTION);
  DEBUGPROFMSG("vt_def_region: "<<tau_vt_name<<": returns "<<FunctionId<<endl;);
#else /* TAU_VAMPIRTRACE */
#ifdef TAU_EPILOG
  static int tau_elg_init=TauInitEpilog();
  string tau_elg_name(string(Name)+" "+string(Type));
  FunctionId = esd_def_region(tau_elg_name.c_str(), ELG_NO_ID, ELG_NO_LNO,
			      ELG_NO_LNO, GroupName, ELG_FUNCTION);
  DEBUGPROFMSG("elg_def_region: "<<tau_elg_name<<": returns "<<FunctionId<<endl;);
#else
#ifdef TAU_SILC
  static int tau_silc_init=0;
  if (tau_silc_init == 0) {
    SILC_InitMeasurement();
    tau_silc_init = 1;
  }
  string tau_silc_name(string(Name)+" "+string(Type));
  FunctionId =  SILC_DefineRegion( tau_silc_name.c_str(),
				   SILC_INVALID_SOURCE_FILE,
				   SILC_INVALID_LINE_NO,
				   SILC_INVALID_LINE_NO,
				   SILC_ADAPTER_COMPILER,
				   SILC_REGION_FUNCTION
				   );

#endif /* TAU_SILC */
#endif /* TAU_EPILOG */
#endif /* TAU_VAMPIRTRACE */
  TauTraceSetFlushEvents(1);
  RtsLayer::UnLockDB();
  
  DEBUGPROFMSG("nct "<< RtsLayer::myNode() <<"," 
	       << RtsLayer::myContext() << ", " << tid 
	       << " FunctionInfo::FunctionInfo(n,t) : Name : "<< GetName() 
	       << " Group :  " << GetProfileGroup()
	       << " Type : " << GetType() << endl;);
  
#ifdef TAU_PROFILEMEMORY
  MemoryEvent = new TauUserEvent(string(string(Name)+" "+Type+" - Heap Memory Used (KB)").c_str());
#endif /* TAU_PROFILEMEMORY */
  
#ifdef TAU_PROFILEHEADROOM
  HeadroomEvent = new TauUserEvent(string(string(Name)+" "+Type+" - Memory Headroom Available (MB)").c_str());
#endif /* TAU_PROFILEHEADROOM */
  
#ifdef RENCI_STFF
  for (int t=0; t < TAU_MAX_THREADS; t++) {
    for (int i=0; i < TAU_MAX_COUNTERS; i++) {
      Signatures[t][i] = NULL;
    }
  }
#endif //RENCI_STFF
  
  return;
}

//////////////////////////////////////////////////////////////////////
FunctionInfo::FunctionInfo(const char *name, const char *type, 
	TauGroup_t ProfileGroup , const char *ProfileGroupName, bool InitData,
	int tid) {
  DEBUGPROFMSG("FunctionInfo::FunctionInfo: MyProfileGroup_ = " << ProfileGroup 
	       << " Mask = " << RtsLayer::TheProfileMask() <<endl;);
  Name = strdup(name);
  Type = strdup(type);
  
  FunctionInfoInit(ProfileGroup, ProfileGroupName, InitData, tid);
}

//////////////////////////////////////////////////////////////////////

FunctionInfo::FunctionInfo(const char *name, const string& type, 
			   TauGroup_t ProfileGroup , const char *ProfileGroupName, bool InitData,
			   int tid) {
  Name = strdup(name);
  Type = strdup(type.c_str());
  
  FunctionInfoInit(ProfileGroup, ProfileGroupName, InitData, tid);
}

//////////////////////////////////////////////////////////////////////

FunctionInfo::FunctionInfo(const string& name, const char * type, 
	TauGroup_t ProfileGroup , const char *ProfileGroupName, bool InitData,
	int tid) {
  Name = strdup(name.c_str());
  Type = strdup(type);
  
  FunctionInfoInit(ProfileGroup, ProfileGroupName, InitData, tid);
}

//////////////////////////////////////////////////////////////////////

FunctionInfo::FunctionInfo(const string& name, const string& type, 
	TauGroup_t ProfileGroup , const char *ProfileGroupName, bool InitData,
	int tid) {
  
  Name = strdup(name.c_str());
  Type = strdup(type.c_str());
  
  FunctionInfoInit(ProfileGroup, ProfileGroupName, InitData, tid);
}

//////////////////////////////////////////////////////////////////////

FunctionInfo::~FunctionInfo()
{
// Don't delete Name, Type - if dtor of static object dumps the data
// after all these function objects are destroyed, it can't get the 
// name, and type.
//	delete [] Name;
//	delete [] Type;
  TheSafeToDumpData() = 0;
}

double * FunctionInfo::GetExclTime(int tid){
  double * tmpCharPtr = (double *) malloc( sizeof(double) * Tau_Global_numCounters);
  for(int i=0;i<Tau_Global_numCounters;i++){
    tmpCharPtr[i] = ExclTime[tid][i];
  }
  return tmpCharPtr;
}

double * FunctionInfo::GetInclTime(int tid){
  double * tmpCharPtr = (double *) malloc( sizeof(double) * Tau_Global_numCounters);
  for(int i=0;i<Tau_Global_numCounters;i++){
    tmpCharPtr[i] = InclTime[tid][i];
  }
  return tmpCharPtr;
}


double *FunctionInfo::getInclusiveValues(int tid) {
  printf ("TAU: Warning, potentially evil function called\n");
  return InclTime[tid];
}

double *FunctionInfo::getExclusiveValues(int tid) {
  printf ("TAU: Warning, potentially evil function called\n");
  return ExclTime[tid];
}

void FunctionInfo::getInclusiveValues(int tid, double *values) {
  for(int i=0; i<Tau_Global_numCounters; i++) {
    values[i] = InclTime[tid][i];
  }
}

void FunctionInfo::getExclusiveValues(int tid, double *values) {
  for(int i=0; i<Tau_Global_numCounters; i++) {
    values[i] = ExclTime[tid][i];
  }
}


//////////////////////////////////////////////////////////////////////
long FunctionInfo::GetFunctionId(void) {
  // To avoid data races, we use a lock if the id has not been created
  if (FunctionId == 0) {
#ifdef DEBUG_PROF
    printf("Fid = 0! \n");
#endif // DEBUG_PROF
    while (FunctionId ==0) {
      RtsLayer::LockDB();
      RtsLayer::UnLockDB();
    }
  }
  return FunctionId;
}
	    

//////////////////////////////////////////////////////////////////////
void FunctionInfo::ResetExclTimeIfNegative(int tid) { 
  /* if exclusive time is negative (at Stop) we set it to zero during
     compensation. This function is used to reset it to zero for single
     and multiple counters */
  int i;
  for (i=0; i < Tau_Global_numCounters; i++) {
    if (ExclTime[tid][i] < 0) {
      ExclTime[tid][i] = 0.0; /* set each negative counter to zero */
    }
  }
  return; 
}



//////////////////////////////////////////////////////////////////////
void tauCreateFI(void **ptr, const char *name, const char *type, 
		 TauGroup_t ProfileGroup , const char *ProfileGroupName) {
  if (*ptr == 0) {

#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::LockDB();
#else
    RtsLayer::LockDB();
#endif
    if (*ptr == 0) {
      *ptr = new FunctionInfo(name, type, ProfileGroup, ProfileGroupName);
    }
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::UnLockDB();
#else
    RtsLayer::UnLockDB();
#endif

  }
}

void tauCreateFI(void **ptr, const char *name, const string& type, 
		 TauGroup_t ProfileGroup , const char *ProfileGroupName) {
  if (*ptr == 0) {
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::LockDB();
#else
    RtsLayer::LockDB();
#endif
    if (*ptr == 0) {
      *ptr = new FunctionInfo(name, type, ProfileGroup, ProfileGroupName);
    }
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::UnLockDB();
#else
    RtsLayer::UnLockDB();
#endif
  }
}

void tauCreateFI(void **ptr, const string& name, const char *type, 
		 TauGroup_t ProfileGroup , const char *ProfileGroupName) {
  if (*ptr == 0) {
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::LockDB();
#else
    RtsLayer::LockDB();
#endif
    if (*ptr == 0) {
      *ptr = new FunctionInfo(name, type, ProfileGroup, ProfileGroupName);
    }
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::UnLockDB();
#else
    RtsLayer::UnLockDB();
#endif
  }
}

void tauCreateFI(void **ptr, const string& name, const string& type, 
		 TauGroup_t ProfileGroup , const char *ProfileGroupName) {
  if (*ptr == 0) {
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::LockDB();
#else
    RtsLayer::LockDB();
#endif
    if (*ptr == 0) {
      *ptr = new FunctionInfo(name, type, ProfileGroup, ProfileGroupName);
    }
#ifdef TAU_CHARM
    if (RtsLayer::myNode() != -1)
      RtsLayer::UnLockDB();
#else
    RtsLayer::UnLockDB();
#endif
  }
}
/***************************************************************************
 * $RCSfile: FunctionInfo.cpp,v $   $Author: amorris $
 * $Revision: 1.82 $   $Date: 2009/12/21 17:58:01 $
 * VERSION_ID: $Id: FunctionInfo.cpp,v 1.82 2009/12/21 17:58:01 amorris Exp $ 
 ***************************************************************************/
