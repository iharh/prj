#pragma once
#include "SpecManager.h"
#include "pin.h" 
// !!! include<mpi.h>
#include<list>
#include<fstream>

//!!! #define TAUPIN_TRACE instead of TRACE
//some tracing macros
#ifdef TAUPIN_TRACE
#define DBG_TRACE(exp) cout<<__FUNCTION__<<">:"<<exp<<endl;\
						 fflush(stdout) 
#define DBG_TR_EXIT(exp) cout<<__FUNCTION__<<">:"<<exp<<endl;\
						 fflush(stdout);\
						 exit(1)
#define DBG_TR_RET(exp) cout<<__FUNCTION__<<">:"<<exp<<endl;\
						fflush(stdout);\
						return 
#define DBG_TR_RETNULL(exp) cout<<__FUNCTION__<<">:"<<exp<<endl;\
							fflush(stdout);\
							return NULL
#define DBG_TR_RETZERO(exp)	cout<<__FUNCTION__<<">:"<<exp<<endl;\
							fflush(stdout);\
							return 0
#else 
#define DBG_TRACE(exp)
#define DBG_TR_EXIT(exp)
#define DBG_TR_RET(exp) 
#define DBG_TR_RETNULL(exp)
#define DBG_TR_RETZERO(exp)
#endif

//structure passed to 
//track the instrumentation 
struct RtnTrack{
	int stage; //not yet used 
	int flag; //flag
	string rtn; //rtn name 
	string img; //imagename
	void *tau; //some handel for TAU instrumentor
};

//Manages the tracing 
//knows TAU and PIN both
class TraceManager
{
private:	
	string img; //image name
	int rtncnt;
	int procid_0;
	SpecManager *Spm; //keep the specification manager
	string ImageTrim(string image);
	list<RtnTrack*> bef_list;
	list<RtnTrack*> aft_list;
	ofstream trace_file;
	bool mpi_setup;
public:
	
	TraceManager(SpecManager* spm );
	TraceManager(SpecManager* spm,string img);
	~TraceManager(void);
	//Apply instrumentation on an image by consulting spec manager
	void InstApply(IMG img);
	void InstApply();
	//before and after execute of the rtn block
	void BeforeExec(RtnTrack* rtntr);
	void AfterExec(RtnTrack* rtntr);
	//this is the function which will dump everything at the last
	void EndTrace();
	bool IsInstSafe(string rtn_name);
	bool IsMpiRtn(string rtn_name);
	bool IsNormal(RTN myrtn);
	void LogMessage(string msg);
//!!!
	void ThreadStart();
	void ThreadEnd();
};


extern "C" {
// !!! delete HMPI_stuff

//!!!
	void DoThreadStart();
	void DoThreadEnd();


   void DumpTrace();
   void StartProfile(char* rtn, void **tauptr); 
   void EndProfile(char *rtn,void * tau);
   void MpiSetUp();
   void TauTest();
   void SetupProfileFile();
};






