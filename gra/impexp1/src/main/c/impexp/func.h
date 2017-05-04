#pragma once

#ifdef  __cplusplus
extern "C" {
#endif

/* !!! clb !!!
define FEXPORT __attribute__((visibility("default")))
extern "C" does not compile - dislike ??? string ???
*/
#if defined(_MSC_VER)
    #define FEXPORT __declspec(dllexport)
#elif defined(__GNUC__)
    #define FEXPORT __attribute__ ((visibility("default")))
#else
    #error Unsupported compiler
#endif

#define NO  0
#define YES 1
#define UNK 2

/* Automaton structures */

/** Main automaton structure */
struct fsm {
  char name[40];
  int arity;
  int arccount;
  int statecount;
  int linecount;
  int finalcount;
  long long pathcount;
  int is_deterministic;
  int is_pruned;
  int is_minimized;
  int is_epsilon_free;
  int is_loop_free;
  int is_completed;
  int arcs_sorted_in;
  int arcs_sorted_out;
  struct fsm_state *states;             /* pointer to first line */
  struct sigma *sigma;
  struct medlookup *medlookup;
};

/* Minimum edit distance structure */

struct medlookup {
    int *confusion_matrix;      /* Confusion matrix */
};

/** Array of states */
struct fsm_state {
    int state_no; /* State number */
    short int in ;
    short int out ;
    int target;
    char final_state ;
    char start_state ;
};

/** Linked list of sigma */
/** number < IDENTITY is reserved for special symbols */
struct sigma {
    int number;
    char *symbol;
    struct sigma *next;
};

//FEXPORT void exp_func(void);
FEXPORT struct fsm *fsm_minimize(struct fsm *net);

#ifdef  __cplusplus
}
#endif
