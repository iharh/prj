#include "func.h"

#include <stdlib.h>

/*FEXPORT
void
exp_func(void) {
}*/

FEXPORT
struct fsm *fsm_minimize(struct fsm *net) {
/*    extern int g_minimal;
    extern int g_minimize_hopcroft;

    if (net == NULL) { return NULL; }
*/
    /* The network needs to be deterministic and trim before we minimize */
/*
    if (net->is_deterministic != YES)
        net = fsm_determinize(net);
    if (net->is_pruned != YES)
        net = fsm_coaccessible(net);
    if (net->is_minimized != YES && g_minimal == 1) {
        if (g_minimize_hopcroft != 0) {
            net = fsm_minimize_hop(net);
        }
        else 
            net = fsm_minimize_brz(net);        
        fsm_update_flags(net,YES,YES,YES,YES,UNK,UNK);
    }
*/
    return(net);
}
