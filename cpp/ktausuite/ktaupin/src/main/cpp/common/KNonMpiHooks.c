#define TAU_WINDOWS
#define TAU_MAX_REQUESTS  4096
#define PROFILING_ON
#include <Profile/Profiler.h>

void
SetupProfileFile()
{
	TAU_PROFILE_SET_NODE(0);
}

void
DumpTrace()
{
	//dont do anything here for now.
	//TAU_DB_DUMP();
}

void
StartProfile(char *rtn , void **tauptr)
{	
	//For now we are doing a phase profiling 
	//but will have to llok for other options
    /*
	Tau_profile_c_timer(tauptr, rtn, " ",TAU_USER, "TAU_USER" );
	Tau_start_timer(*tauptr, 0);
	printf("started tau\n");
	fflush(stdout);
	*tauptr=tauptr1; 
	*/
	TAU_STATIC_PHASE_START(rtn);
	//TAU_PROFILER_CREATE(*tauptr, rtn,"", TAU_USER);
    //TAU_PROFILER_START(*tauptr);
}

void
EndProfile(char *rtn, void *tau)
{
	//For now we are doing a phase profiling 
	//but will have to llok for other options
	/*
    Tau_stop_timer(tau);
	printf("Ended tau\n");
	fflush(stdout);
	*/
	TAU_STATIC_PHASE_STOP(rtn);
	//TAU_PROFILER_STOP(tau); 
}

//!!!
void
DoThreadStart()
{
	TAU_REGISTER_THREAD();
}

void
DoThreadEnd()
{
}



//include <stdio.h>
//include <stdlib.h>

//just for testing 
//nothing to look
/*!!!
void TauTest()
{
	int i,sum=0;
    TAU_PROFILE_TIMER(tautimer, "TEST",  " ", TAU_MESSAGE);
    TAU_PROFILE_START(tautimer);
    printf("I'm here\n");
	for(i=0;i<100000;i++)
	{
		sum*=i;
	}
	TAU_PROFILE_STOP(tautimer);
}
*/
