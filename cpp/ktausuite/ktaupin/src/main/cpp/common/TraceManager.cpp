/***************************************************************************************************
This file contains the tracemanger module of TAUPIN. 
This module interacts with both PIN and TAU systems   
****************************************************************************************************/

#include "TraceManager.h"
//!!! include "MpiHooks.h"
extern VOID HookHandleBefore(RtnTrack* rtntr);
extern VOID HookHandleAfter(RtnTrack* rtntr);
extern VOID HookHandle(RtnTrack* rtntr);
extern VOID HookHandle1(RtnTrack* rtntr);
extern SpecManager* mySpec;
VOID MpiInst(IMG img);
VOID MpiInstRtn(RTN myrtn);

TraceManager::TraceManager(SpecManager* spm)
{
	this->Spm=spm;
	this->rtncnt=0;
	mpi_setup=false;
}

TraceManager::TraceManager(SpecManager* spm,string img)
{
	this->Spm=spm;
	this->rtncnt=0;
	this->img=img;
	mpi_setup=false;
	//trace_file.open("debug_trace",ios_base::app);
}

TraceManager::~TraceManager(void)
{
	//trace_file.close();
}

void TraceManager::LogMessage(string msg)
{
	ofstream mtrace_file;
	mtrace_file.open("debug_trace",ios_base::app);
	mtrace_file.write(msg.c_str(),msg.length());
	//trace_file.flush();
	mtrace_file.close();
}

//apply the instrumentation specification
void TraceManager::InstApply()
{	
}

string TraceManager::ImageTrim(string image)
{
	//remove all the path information 
	//get only the module name 
	int imglen=image.length();
	int pos=image.find_last_of('\\',imglen);
	pos++;
	return image.substr(pos,imglen-pos);
}


string unsafeRtns[]={
//!!!
	"_on_process_enter",

	"?_Reserve@?$vector@V?$shared_ptr@Voption_description@program_options@boost@@@boost@@V?$allocator@V?$shared_ptr@Voption_description@program_options@boost@@@boost@@@std@@@std@@IAEXI@Z"
//	"?_Reserve@?$vector@V?$shared_ptr@Voption_description@program_options@boost@@@boost@@V?$allocator@V?$shared_ptr@Voption_description@program_options@boost@@@boost@@@std@@@std@@IAEXI@Z"
//	"?push_back@?$vector@V?$shared_ptr@Voption_description@program_options@boost@@@boost@@V?$allocator@V?$shared_ptr@Voption_description@program_options@boost@@@boost@@@std@@@std@@QAEXABV?$shared_ptr@Voption_description@program_options@boost@@@boost@@@Z"

//!!!
	"_RTC_",
  "__CRT_RTC_",
  "pre_c_init",
  "__controlfp_s",
  "mainCRTStartup",
  "__tmainCRTStartup",
  "WinMainCRTStartup",
  "__crt",
  "_initp_misc_winxfltr",
  "memset",
  "_setdefaultprecision",
  "__initterm",
  "pre_cpp_init",
  "atexit",
  "_onexit",
  "_mtinit",
  "_initptd",
  "_except_handler",
  "@_EH4_CallFilterFunc",
  "DebuggerProbe",
  "END" };

bool TraceManager::IsInstSafe(string rtn_name)
{
	int i=0;
	while(unsafeRtns[i]!="END")
	{
		string rtn_pref=unsafeRtns[i];
		int status=rtn_name.compare(0,rtn_pref.length(),rtn_pref);
		if(status==0)
			return false;
		i++;
	}
	
	//more checks here for future
	return true;
}

bool TraceManager::IsNormal(RTN myrtn)
{
	BBL my_bbl=RTN_BblTail(myrtn);	
	if(BBL_Valid(my_bbl))
	{
		INS my_ins=BBL_InsTail(my_bbl);
		while(INS_Valid(my_ins))
		{
			if(INS_IsRet(my_ins))
			{
				//cerr<<"Normal Routine::"<<RTN_Name(myrtn)<<endl;
				return true;
			}
			my_ins=INS_Prev(my_ins);
		}	
		/*if(INS_IsBranch(my_ins)|| INS_IsNop(my_ins))
		{
			cerr<<"!!!Abnormal Routine::"<<RTN_Name(myrtn)<<endl;
			return false;
		}	
		my_bbl=BBL_Prev(my_bbl); */
	}
	//cerr<<"!!!!Abnormal Routine::"<<RTN_Name(myrtn)<<endl;
	return false;
}

bool TraceManager::IsMpiRtn(string rtn_name)
{
	if(rtn_name.at(0)=='_')
		rtn_name=rtn_name.substr(1);
	int indx=rtn_name.find_first_of('@');
	if(indx!=string::npos)
		rtn_name=rtn_name.substr(0,indx);
	if(rtn_name.length()==0)
		return false;
	return mySpec->IsMpiRtn(rtn_name);

	/*
	string rtn_pref="MPI_";
	int status=rtn_name.compare(0,rtn_pref.length(),rtn_pref);
	int status1=rtn_name.compare(1,rtn_pref.length(),rtn_pref);
	if(status==0 || status1==0 )
	{
		if(!mpi_setup)
		{
			MpiSetUp(); 
			mpi_setup=false;
		}
		return true;
	}
	return false;
	*/
}

void TraceManager::InstApply(IMG img)
{

	DBG_TRACE("before check");
	DBG_TRACE(IMG_Name(img));
	
	if(mySpec==NULL){
		cerr<<"Spec manager is Null"<<endl;		
		return;
	}

	string imgname=ImageTrim(IMG_Name(img));
	cerr<<imgname<<endl;
/*!!!
	if(imgname.compare(MPI_LIB)==0)
	{
		//if the module is mpi dll then 
		//MpiInst(img);
		return;
	}
*/
    
	//for rest of the modules it goes ahead here
	//checks if it's to be instrumented 
	//leaves if it's not suppose to be
	if(!mySpec->InstImage(imgname))
	{
		DBG_TRACE("Not to be instrumented");
		return;
	}
	//cerr<<"After approved for image inst"<<endl;	

	//Start enumarting all the routines in the image to be instrumented 
	SEC temp=IMG_SecHead(img);
	while(SEC_Valid(temp))
	{
		//we pick only executable code segment
		if(SEC_Type(temp)==SEC_TYPE_EXEC){
			RTN myrtn=SEC_RtnHead(temp);
			DBG_TRACE("RTNs to be instrumented");
			while(RTN_Valid(myrtn))
			{

				DBG_TRACE(RTN_Name(myrtn));
				//cerr<<RTN_Name(myrtn)<<endl;

				if(IsMpiRtn(RTN_Name(myrtn)))
				{
#ifndef NOMPI
					MpiInstRtn(myrtn);
#endif
					myrtn=RTN_Next(myrtn);
					continue;
				}

				if(RTN_Name(myrtn).length()==0 || !IsInstSafe(RTN_Name(myrtn)))
				{
					myrtn=RTN_Next(myrtn);
					continue;
				}
				
				if(mySpec->InstRtn(RTN_Name(myrtn),imgname))
				{
					//if the routine is to be instrumented 
					//apply the instrumentation here
					RTN_Open(myrtn);
					if(!IsNormal(myrtn))
					{
						RTN_Close(myrtn);
						myrtn=RTN_Next(myrtn);
						continue;
					}
					//DBG_TRACE(RTN_Name(myrtn));
					RtnTrack * brtntr = new RtnTrack;
					brtntr->img=imgname;
					brtntr->rtn=RTN_Name(myrtn);
					brtntr->stage=0;
					brtntr->flag=mySpec->GetProfileFlag(brtntr->rtn,brtntr->img);

					//insert the instrumentation at the exit point
					
					RTN_InsertCall(myrtn, IPOINT_AFTER, (AFUNPTR)HookHandleAfter, IARG_PTR ,brtntr, IARG_END);
					//RTN_InsertCallProbed(myrtn, IPOINT_AFTER, (AFUNPTR)HookHandleAfter, IARG_PTR ,brtntr, IARG_END);
					//insert the instrumentation at the entry point
					RTN_InsertCall(myrtn, IPOINT_BEFORE, (AFUNPTR)HookHandleBefore, IARG_PTR ,brtntr, IARG_END);
					//RTN_InsertCallProbed(myrtn, IPOINT_BEFORE, (AFUNPTR)HookHandleBefore, IARG_PTR ,brtntr, IARG_END);
					//cerr<<"Name::"<<RTN_Name(myrtn)<<" Start::"<<RTN_Address(myrtn)<<"  End::"<<RTN_Address(myrtn)+RTN_Size(myrtn)<<endl;	
					RTN_Close(myrtn);
				}
				myrtn=RTN_Next(myrtn);
			}
		}
		temp=SEC_Next(temp);		
	}	
}
	//before and after execute of the rtn block
void TraceManager::BeforeExec(RtnTrack* rtntr)
{
	//this is called before entry to the function
	int pflag=rtntr->flag;
	//bef_list.push_back(rtntr);
   	//more complicated option checking can done later
	if(pflag&&1)
	{
		DBG_TRACE("Profile Start");
		//start TAU profiling
		StartProfile((char*)rtntr->rtn.c_str(),&rtntr->tau);
	}
}

void TraceManager::AfterExec(RtnTrack* rtntr)
{
	//handles just before exit
	int pflag=rtntr->flag;
	//aft_list.push_back(rtntr);
	if(pflag&&1)
	{
		DBG_TRACE("Profile Stop");
		//calls enf of profile into tau system
		EndProfile((char*)rtntr->rtn.c_str(),rtntr->tau);
	}    
}

//this is the function which will dump everything at the last
void TraceManager::EndTrace()
{
	cerr<<"Tracing ended here"<<endl;
	//this doesnt do anything as of now
	bef_list.sort();
	aft_list.sort();
	list<RtnTrack*>::iterator it1,it2;
	it2=aft_list.begin();
	for(it1=bef_list.begin();it1!=bef_list.end();it1++)
	{
		if(it2!=aft_list.end())
		{
			cerr<<*it2;
			it2++;
		}
	}
	DumpTrace();
}

//!!!
void
TraceManager::ThreadStart()
{
	DoThreadStart();
}

//!!!
void
TraceManager::ThreadEnd()
{
	DoThreadEnd();
}

RTN RTN_FindLoop(IMG img, string rtn_name)
{
	SEC temp=IMG_SecHead(img);
	while(SEC_Valid(temp))
	{
		//we pick only executable code segment
		if(SEC_Type(temp)==SEC_TYPE_EXEC){
			RTN myrtn=SEC_RtnHead(temp);
			DBG_TRACE("RTNs to be instrumented");
			while(RTN_Valid(myrtn))
			{
				cerr<<RTN_Name(myrtn)<<endl;
				if(RTN_Name(myrtn)==rtn_name)
				{
					cerr<<"Rtn Found:"<<rtn_name<<endl;
					return myrtn;
				}
				myrtn=RTN_Next(myrtn);
			}
		}
		temp=SEC_Next(temp);
	}
	RTN empty;
	return empty;
}
