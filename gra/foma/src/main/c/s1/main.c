#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <limits.h>
#include <time.h>

#include "smallrt.h"

/* Special symbols on arcs */
#define EPSILON 0
#define UNKNOWN 1
#define IDENTITY 2

/* Flag types */
#define FLAG_UNIFY 1
#define FLAG_CLEAR 2
#define FLAG_DISALLOW 4
#define FLAG_NEGATIVE 8
#define FLAG_POSITIVE 16
#define FLAG_REQUIRE 32
#define FLAG_EQUAL 64

#define NO  0

typedef void *fsm_read_binary_handle;

struct io_buf_handle {
    char *my_io_buf;
    char *io_buf_ptr;
};

/** Linked list of sigma */
/** number < IDENTITY is reserved for special symbols */
struct sigma {
    int number;
    char *symbol;
    struct sigma *next;
};

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

// apply_up stuff

struct apply_handle {

    int ptr;
    int curr_ptr;
    int ipos;
    int opos;
    int mode;
    int printcount;
    int *numlines;
    int *statemap;
    int *marks;

    struct sigma_trie {
        int signum;
        struct sigma_trie *next;
    } *sigma_trie;

    struct sigmatch_array {
        int signumber ;
        int consumes ;
    } *sigmatch_array;

    struct sigma_trie_arrays {
        struct sigma_trie *arr;
        struct sigma_trie_arrays *next;
    } *sigma_trie_arrays;

    int binsearch;
    int indexed;
    int state_has_index;
    int sigma_size;
    int sigmatch_array_size;
    int current_instring_length;
    int has_flags;
    int obey_flags;
    int show_flags;
    int print_space;
    char *space_symbol;
    char *separator;
    char *epsilon_symbol;
    int print_pairs;
    int apply_stack_ptr;
    int apply_stack_top;
    int oldflagneg;
    int outstringtop;
    int iterate_old;
    int iterator;
    uint8_t *flagstates;
    char *outstring;
    char *instring;
    struct sigs {
        char *symbol;
        int length;
    } *sigs;
    char *oldflagvalue;
    
    struct fsm *last_net;
    struct fsm_state *gstates;
    struct sigma *gsigma;
    struct apply_state_index {
        int fsmptr;
        struct apply_state_index *next;
    } **index_in, **index_out, *iptr;

    struct flag_list {
        char *name;
        char *value;
        short int neg;
        struct flag_list *next;
    } *flag_list;

    struct flag_lookup {
        int type;
        char *name;
        char *value;
    } *flag_lookup ;

    struct searchstack {
        int offset;
        struct apply_state_index *iptr;
        int state_has_index;
        int opos;
        int ipos;
        int visitmark;
        char *flagname;
        char *flagvalue;
        int flagneg;
    } *searchstack ;
};

// done with foma.h stuff




struct sigma *my_sigma_create() {
  struct sigma *sigma;
  sigma = xxmalloc(sizeof(struct sigma));
  sigma->number = -1; /*Empty sigma*/
  sigma->next = NULL;
  sigma->symbol = NULL;
  return(sigma);
}

struct io_buf_handle *
my_io_init() {
    struct io_buf_handle *iobh;
    iobh = xxmalloc(sizeof(struct io_buf_handle));
    (iobh->my_io_buf) = NULL;
    (iobh->io_buf_ptr) = NULL;
    return(iobh);
}

void
my_io_free(struct io_buf_handle *iobh) {
    if (iobh->my_io_buf != NULL) {
        xxfree(iobh->my_io_buf);
        (iobh->my_io_buf) = NULL;
    }
    xxfree(iobh);
}

size_t
io_text_file_to_mem(struct io_buf_handle *iobh, char *filename) {
    size_t size;
    //gzFile FILE;
    //size = io_get_file_size(filename);
    FILE *f = fopen(filename, "rb");
    fseek(f, 0, SEEK_END);
    size = ftell(f);
    fseek(f, 0, SEEK_SET);  //same as rewind(f);

    if (size == 0) {
        return 0;
    }
    (iobh->my_io_buf) = xxmalloc((size+1)*sizeof(char));

    //FILE = gzopen(filename, "rb");
    //gzread(FILE, iobh->my_io_buf, size);
    //gzclose(FILE);

    fread(iobh->my_io_buf, size, 1, f);
    fclose(f);

    *((iobh->my_io_buf)+size) = '\0';
    iobh->io_buf_ptr = iobh->my_io_buf;
    return(size);
}

fsm_read_binary_handle
fsm_read_text_file_multiple_init(char *filename) {
    struct io_buf_handle *iobh;
    fsm_read_binary_handle fsm_read_handle;

    iobh = my_io_init();
    if (io_text_file_to_mem(iobh, filename) == 0) {
	my_io_free(iobh); // TBD: where do we free in a case of success ?
	return NULL;
    }
    fsm_read_handle = (void *) iobh;
    return(fsm_read_handle);
}

static /* clb !!! INLINE*/ int
my_explode_line(char *buf, int *values) {
    int i, j, items;
    j = i = items = 0;
    for (;;) {
        for (i = j; *(buf+j) != ' ' && *(buf+j) != '\0'; j++) { }
        if (*(buf+j) == '\0') {
            *(values+items) = atoi(buf+i);
            items++;
            break;
        } else{
            *(buf+j) = '\0';
            *(values+items) = atoi(buf+i);
            items++;
            j++;
        }
    }
    return(items);
}


int
my_sigma_add_number(struct sigma *sigma, char *symbol, int number) {
    struct sigma *newsigma, *prev_sigma;
    prev_sigma = NULL;
    if (sigma->number == -1) {
        sigma->symbol = xxstrdup(symbol);
        sigma->number = number;
        sigma->next = NULL;
        return(1);
    }
    for (newsigma = sigma; newsigma != NULL; newsigma = newsigma->next) {
        prev_sigma = newsigma;
    }
    newsigma = xxmalloc(sizeof(struct sigma));
    newsigma->symbol = xxstrdup(symbol);
    newsigma->number = number;
    newsigma->next = NULL;
    prev_sigma->next = newsigma;
    return(1);
}

static int
my_io_gets(struct io_buf_handle *iobh, char *target) {
    int i;
    for (i = 0; *((iobh->io_buf_ptr)+i) != '\r' && *((iobh->io_buf_ptr)+i) != '\n' && *((iobh->io_buf_ptr)+i) != '\0'; i++) {
        *(target+i) = *((iobh->io_buf_ptr)+i);
    }
    *(target+i) = '\0';

    if (*((iobh->io_buf_ptr)+i) == '\0')
        (iobh->io_buf_ptr) = (iobh->io_buf_ptr) + i;
    else {
        if ( *((iobh->io_buf_ptr)+(i+1)) == '\r' || *((iobh->io_buf_ptr)+(i+1)) == '\n' ) {
            (iobh->io_buf_ptr) = (iobh->io_buf_ptr) + i + 2;
        } else {
            (iobh->io_buf_ptr) = (iobh->io_buf_ptr) + i + 1;
        }
    }

    return(i);
}

int my_fsm_sigma_destroy(struct sigma *sigma) {
    struct sigma *sig, *sigp;
    for (sig = sigma, sigp = NULL; sig != NULL; sig = sigp) {
	sigp = sig->next;
	if (sig->symbol != NULL) {
	    xxfree(sig->symbol);
	    sig->symbol = NULL;
	}
	xxfree(sig);
    }
    return 1;
}

int my_fsm_destroy(struct fsm *net) {
    if (net == NULL) {
        return 0;
    }
    if (net->medlookup != NULL && net->medlookup->confusion_matrix != NULL) {
        xxfree(net->medlookup->confusion_matrix);
	net->medlookup->confusion_matrix = NULL;
    }
    if (net->medlookup != NULL) {
        xxfree(net->medlookup);
	net->medlookup = NULL;
    }
    my_fsm_sigma_destroy(net->sigma);
    net->sigma = NULL;
    if (net->states != NULL) {
        xxfree(net->states);
	net->states = NULL;
    }
    xxfree(net);
    return(1);
}

struct fsm *my_fsm_create (char *name) {
  struct fsm *fsm;
  fsm = xxmalloc(sizeof(struct fsm));
  strcpy(fsm->name, name);
  fsm->arity = 1;
  fsm->arccount = 0;
  fsm->is_deterministic = NO;
  fsm->is_pruned = NO;
  fsm->is_minimized = NO;
  fsm->is_epsilon_free = NO;
  fsm->is_loop_free = NO;
  fsm->arcs_sorted_in = NO;
  fsm->arcs_sorted_out = NO;
  fsm->sigma = my_sigma_create();
  fsm->states = NULL;
  fsm->medlookup = NULL;
  return(fsm);
}

#define READ_BUF_SIZE 4096

#ifndef ORIGINAL
  #if defined (_MSC_VER) || (__MINGW32__)
    #define LONG_LONG_SPECIFIER "%I64d"
  #else
    #define LONG_LONG_SPECIFIER "%lld"
  #endif
#endif // #ifndef ORIGINAL

struct fsm *
my_io_net_read(struct io_buf_handle *iobh, char **net_name) {

    char buf[READ_BUF_SIZE];
    struct fsm *net;
    struct fsm_state *fsm;
    
    char *new_symbol;
    int i, items, new_symbol_number, laststate, lineint[5], *cm;
    int extras;
    char last_final = '1';

    if (my_io_gets(iobh, buf) == 0) {
        return NULL;
    }
    
    net = my_fsm_create("");

    if (strcmp(buf, "##foma-net 1.0##") != 0) {
	my_fsm_destroy(net);
        perror("File format error foma!\n");
        return NULL;
    }
    my_io_gets(iobh, buf);
    if (strcmp(buf, "##props##") != 0) {
        perror("File format error props!\n");
	my_fsm_destroy(net);
        return NULL;
    }
    /* Properties */
    my_io_gets(iobh, buf);
    extras = 0;
#ifdef ORIGINAL
    sscanf(buf, "%i %i %i %i %i %lld %i %i %i %i %i %i %s", &net->arity, &net->arccount, &net->statecount, &net->linecount, &net->finalcount, &net->pathcount, &net->is_deterministic, &net->is_pruned, &net->is_minimized, &net->is_epsilon_free, &net->is_loop_free, &extras, buf);
#else
    sscanf(buf, "%i %i %i %i %i "LONG_LONG_SPECIFIER" %i %i %i %i %i %i %s", &net->arity, &net->arccount, &net->statecount, &net->linecount, &net->finalcount, &net->pathcount, &net->is_deterministic, &net->is_pruned, &net->is_minimized, &net->is_epsilon_free, &net->is_loop_free, &extras, buf);
#endif
    strcpy(net->name, buf);
    *net_name = xxstrdup(buf);
    my_io_gets(iobh, buf);

    net->is_completed = (extras & 3);
    net->arcs_sorted_in = (extras & 12) >> 2;
    net->arcs_sorted_out = (extras & 48) >> 4;

    /* Sigma */
    while (strcmp(buf, "##sigma##") != 0) { /* Loop until we encounter ##sigma## */
        if (buf[0] == '\0') {
	  printf("File format error at sigma definition!\n");
	  my_fsm_destroy(net);
	  return NULL;
        }
        my_io_gets(iobh, buf);
    }

    for (;;) {
        my_io_gets(iobh, buf);
        if (buf[0] == '#') break;
        if (buf[0] == '\0') continue;
        new_symbol = strstr(buf, " ");
	new_symbol[0] = '\0';
	new_symbol++;
	if (new_symbol[0] == '\0') {
	    sscanf(buf,"%i", &new_symbol_number);
	    my_sigma_add_number(net->sigma, "\n", new_symbol_number);
	} else {
	    sscanf(buf,"%i", &new_symbol_number);
	    my_sigma_add_number(net->sigma, new_symbol, new_symbol_number);
	}
    }

    /* States */
    if (strcmp(buf, "##states##") != 0) {
        printf("File format error!\n");
        return NULL;
    }
    net->states = xxmalloc(net->linecount*sizeof(struct fsm_state));
    fsm = net->states;
    laststate = -1;
    for (i=0; ;i++) {
        my_io_gets(iobh, buf);
        if (buf[0] == '#') break;

        /* scanf is just too slow here */

        //items = sscanf(buf, "%i %i %i %i %i",&lineint[0], &lineint[1], &lineint[2], &lineint[3], &lineint[4]);

        items = my_explode_line(buf, &lineint[0]);

        switch (items) {
        case 2:
            (fsm+i)->state_no = laststate;
            (fsm+i)->in = lineint[0];
            (fsm+i)->out = lineint[0];
            (fsm+i)->target = lineint[1];
            (fsm+i)->final_state = last_final;
            break;
        case 3:
            (fsm+i)->state_no = laststate;
            (fsm+i)->in = lineint[0];
            (fsm+i)->out = lineint[1];
            (fsm+i)->target = lineint[2];
            (fsm+i)->final_state = last_final;
            break;
        case 4:
            (fsm+i)->state_no = lineint[0];
            (fsm+i)->in = lineint[1];
            (fsm+i)->out = lineint[1];
            (fsm+i)->target = lineint[2];
            (fsm+i)->final_state = lineint[3];
            laststate = lineint[0];
            last_final = lineint[3];
            break;
        case 5:
            (fsm+i)->state_no = lineint[0];
            (fsm+i)->in = lineint[1];
            (fsm+i)->out = lineint[2];
            (fsm+i)->target = lineint[3];
            (fsm+i)->final_state = lineint[4];
            laststate = lineint[0];
            last_final = lineint[4];
            break;
        default:
            printf("File format error\n");
            return NULL;
        }
        if (laststate > 0) {
            (fsm+i)->start_state = 0;
        } else if (laststate == -1) {
            (fsm+i)->start_state = -1;
        } else {
            (fsm+i)->start_state = 1;
        }

    }
    /* !!! clb !!! if (strcmp(buf, "##cmatrix##") == 0) {
        cmatrix_init(net);
        cm = net->medlookup->confusion_matrix;
        for (;;) {
            my_io_gets(iobh, buf);
            if (buf[0] == '#') break;
            sscanf(buf,"%i", &i);
            *cm = i;
            cm++;
        }
    }
    */
    if (strcmp(buf, "##end##") != 0) {
        printf("File format error!\n");
        return NULL;
    }
    return(net);
}

struct fsm *
my_fsm_read_binary_file_multiple(fsm_read_binary_handle fsrh) {
    char *net_name;
    struct fsm *net;
    struct io_buf_handle *iobh;
    iobh = (struct io_buf_handle *) fsrh;
    net = my_io_net_read(iobh, &net_name);
    if (net == NULL) {
	my_io_free(iobh);
	return(NULL);
    } else {
	xxfree(net_name);
	return(net);
    }
}

void
my_fsm_count(struct fsm *net) {
  struct fsm_state *fsm;
  int i, linecount, arccount, oldstate, finalcount, maxstate;
  linecount = arccount = finalcount = maxstate = 0;

  oldstate = -1;

  fsm = net->states;
  for (i=0; (fsm+i)->state_no != -1; i++) {
    if ((fsm+i)->state_no > maxstate)
      maxstate = (fsm+i)->state_no;

    linecount++;
    if ((fsm+i)->target != -1) {
        arccount++;
	//        if (((fsm+i)->in != (fsm+i)->out) || ((fsm+i)->in == UNKNOWN) || ((fsm+i)->out == UNKNOWN))
        //    arity = 2;
    }
    if ((fsm+i)->state_no != oldstate) {
        if ((fsm+i)->final_state) {
            finalcount++;
        }
        oldstate = (fsm+i)->state_no;
    }
  }

  linecount++;
  net->statecount = maxstate+1;
  net->linecount = linecount;
  net->arccount = arccount;
  net->finalcount = finalcount;
}

struct fsm *
my_iface_load_stack(char *filename) {
    struct fsm *net;
    fsm_read_binary_handle fsrh;

    if ((fsrh = fsm_read_text_file_multiple_init(filename)) == NULL) {
	fprintf(stderr, "%s: ", filename);
        perror("File error");
        return NULL;
    }
    net = my_fsm_read_binary_file_multiple(fsrh);
    // !!!clb!!! stack_add(net)
    my_fsm_count(net);
    if (strcmp(net->name,"") == 0)
        sprintf(net->name, "%X",rand());

    return net;
}

// apply_up

#define DEFAULT_OUTSTRING_SIZE 4096
#define DEFAULT_STACK_SIZE 128

#define BITMASK(b) (1 << ((b) & 7))
#define BITSLOT(b) ((b) >> 3)
#define BITSET(a,b) ((a)[BITSLOT(b)] |= BITMASK(b))
#define BITCLEAR(a,b) ((a)[BITSLOT(b)] &= ~BITMASK(b))
#define BITTEST(a,b) ((a)[BITSLOT(b)] & BITMASK(b))
#define BITNSLOTS(nb) ((nb + CHAR_BIT - 1) / CHAR_BIT)

void
my_apply_create_statemap(struct apply_handle *h, struct fsm *net) {
    int i;
    struct fsm_state *fsm;
    fsm = net->states;
    h->statemap = xxmalloc(sizeof(int)*net->statecount);
    h->marks = xxmalloc(sizeof(int)*net->statecount);
    h->numlines = xxmalloc(sizeof(int)*net->statecount);

    for (i=0; i < net->statecount; i++) {
	*(h->numlines+i) = 0;  /* Only needed in binary search */
	*(h->statemap+i) = -1;
	*(h->marks+i) = 0;
    }
    for (i=0; (fsm+i)->state_no != -1; i++) {
	*(h->numlines+(fsm+i)->state_no) = *(h->numlines+(fsm+i)->state_no)+1;
	if (*(h->statemap+(fsm+i)->state_no) == -1) {
	    *(h->statemap+(fsm+i)->state_no) = i;
	}
    }
}

void
my_apply_stack_clear (struct apply_handle *h) {
    h->apply_stack_ptr = 0;
}

int
my_sigma_max(struct sigma *sigma) {
  int i;
  if (sigma == NULL)
    return -1;
  for (i=-1; sigma != NULL; sigma = sigma->next)
      i = sigma->number > i ? sigma->number : i;
  return(i);
}

int
my_utf8skip(char *str) {
  unsigned char s;

  s = (unsigned char)(unsigned int) (*str);
  if (s < 0x80)
    return 0;
  if ((s & 0xe0) == 0xc0) {
    return 1;
  }
  if ((s & 0xf0) == 0xe0) {
    return 2;
  }
  if ((s & 0xf8) == 0xf0) {
    return 3;
  }
  return -1;
}

int
my_flag_check(char *s) {
    
    /* We simply simulate this regex (where ND is not dot) */
    /* "@" [U|P|N|R|E|D] "." ND+ "." ND+ "@" | "@" [D|R|C] "." ND+ "@" */
    /* and return 1 if it matches */

    int i;
    i = 0;
    
    if (*(s+i) == '@') { i++; goto s1; } return 0;
 s1:
    if (*(s+i) == 'C') { i++; goto s4; }
    if (*(s+i) == 'N' || *(s+i) == 'E' || *(s+i) == 'U' || *(s+i) == 'P') { i++; goto s2; }
    if (*(s+i) == 'R' || *(s+i) == 'D') { i++; goto s3; } return 0;
 s2:
    if (*(s+i) == '.') { i++; goto s5; } return 0;
 s3:
    if (*(s+i) == '.') { i++; goto s6; } return 0;
 s4:
    if (*(s+i) == '.') { i++; goto s7; } return 0;
 s5:
    if (*(s+i) != '.' && *(s+i) != '\0') { i++; goto s8; } return 0;
 s6:
    if (*(s+i) != '.' && *(s+i) != '\0') { i++; goto s9; } return 0;
 s7:
    if (*(s+i) != '.' && *(s+i) != '\0') { i++; goto s10; } return 0;
 s8:
   if (*(s+i) == '.') { i++; goto s7; }
   if (*(s+i) != '.' && *(s+i) != '\0') { i++; goto s8; } return 0;
 s9:
    if (*(s+i) == '@') { i++; goto s11; }
    if (*(s+i) == '.') { i++; goto s7; }
    if (*(s+i) != '.' && *(s+i) != '\0') { i++; goto s9; } return 0;

 s10:
    if (*(s+i) == '@') {i++; goto s11;}
    if (*(s+i) != '.' && *(s+i) != '\0') { i++; goto s10; } return 0;
 s11:
    if (*(s+i) == '\0') {return 1;} return 0;
}

char *
my_flag_get_name(char *string) {
    int i, start, end, len;
    start = end = 0;
    len = strlen(string);

    for (i=0; i < len; i += (my_utf8skip(string+i) + 1)) {
	if (*(string+i) == '.' && start == 0) {
	    start = i+1;
	    continue;
	}
	if ((*(string+i) == '.' || *(string+i) == '@')  && start != 0) {
	    end = i;
	    break;
	}
    }
    if (start > 0 && end > 0) {
	return(xxstrndup(string+start,end-start));
    }
    return NULL;
}

int
my_flag_get_type(char *string) {
    if (strncmp(string+1,"U.",2) == 0) {
	return FLAG_UNIFY;
    }
    if (strncmp(string+1,"C.",2) == 0) {
	return FLAG_CLEAR;
    }
    if (strncmp(string+1,"D.",2) == 0) {
	return FLAG_DISALLOW;
    }
    if (strncmp(string+1,"N.",2) == 0) {
	return FLAG_NEGATIVE;
    }
    if (strncmp(string+1,"P.",2) == 0) {
	return FLAG_POSITIVE;
    }
    if (strncmp(string+1,"R.",2) == 0) {
	return FLAG_REQUIRE;
    }
    if (strncmp(string+1,"E.",2) == 0) {
	return FLAG_EQUAL;
    }
    return 0;
}

char *
my_flag_get_value(char *string) {
    int i, first, start, end, len;
    first = start = end = 0;
    len = strlen(string);

    for (i=0; i < len; i += (my_utf8skip(string+i) + 1)) {
	if (*(string+i) == '.' && first == 0) {
	    first = i+1;
	    continue;
	}
	if (*(string+i) == '@' && start != 0) {
	    end = i;
	    break;
	}
	if (*(string+i) == '.' && first != 0) {
	    start = i+1;
	    continue;
	}
    }
    if (start > 0 && end > 0) {
	return(xxstrndup(string+start,end-start));
    }
    return NULL;
}

void
my_apply_add_flag(struct apply_handle *h, char *name) {
    struct flag_list *flist, *flist_prev;
    if (h->flag_list == NULL) {
	flist = h->flag_list = xxmalloc(sizeof(struct flag_list));
    } else {
	for (flist = h->flag_list; flist != NULL; flist_prev = flist, flist = flist->next) {
	    if (strcmp(flist->name, name) == 0) {
		return;
	    }
	}
	flist = xxmalloc(sizeof(struct flag_list));
	flist_prev->next = flist;
    }
    flist->name = name;
    flist->value = NULL;
    flist->neg = 0;
    flist->next = NULL;
    return;
}

void
my_apply_mark_flagstates(struct apply_handle *h) {
    int i;
    struct fsm_state *fsm;

    /* Create bitarray with those states that have a flag symbol on an arc */
    /* This is needed to decide whether we can perform a binary search.    */

    if (!h->has_flags || h->flag_lookup == NULL) {
	return;
    }
    if (h->flagstates) {
	xxfree(h->flagstates);
    }
    h->flagstates = xxcalloc(BITNSLOTS(h->last_net->statecount), sizeof(uint8_t));
    fsm = h->last_net->states;
    for (i=0; (fsm+i)->state_no != -1; i++) {
	if ((fsm+i)->target == -1) {
	    continue;
	}
	if ((h->flag_lookup+(fsm+i)->in)->type) {
	    BITSET(h->flagstates,(fsm+i)->state_no);
	}
	if ((h->flag_lookup+(fsm+i)->out)->type) {
	    BITSET(h->flagstates,(fsm+i)->state_no);
	}
    }
}

void
my_apply_add_sigma_trie(struct apply_handle *h, int number, char *symbol, int len) {

    /* Create a trie of sigma symbols (prefixes) so we can    */
    /* quickly (in O(n)) tokenize an arbitrary string into    */
    /* integer sequences representing symbols, using longest- */
    /* leftmost factorization.                                */

    int i;
    struct sigma_trie *st;
    struct sigma_trie_arrays *sta;

    st = h->sigma_trie;
    for (i = 0; i < len; i++) {
	st = st+(unsigned char)*(symbol+i);
	if (i == (len-1)) {
	    st->signum = number;
	} else {
	    if (st->next == NULL) {
		st->next = xxcalloc(256,sizeof(struct sigma_trie));
		st = st->next;
		/* store these arrays to free them later */
		sta = xxmalloc(sizeof(struct sigma_trie_arrays));
		sta->arr = st;
		sta->next = h->sigma_trie_arrays;
		h->sigma_trie_arrays = sta;
	    } else {
		st = st->next;
	    }
	}
    }
}

void
my_apply_create_sigarray(struct apply_handle *h, struct fsm *net) {
    struct sigma *sig;
    int i, maxsigma;
    
    maxsigma = my_sigma_max(net->sigma);
    h->sigma_size = maxsigma+1;
    // Default size created at init, resized later if necessary
    h->sigmatch_array = xxcalloc(1024,sizeof(struct sigmatch_array));
    h->sigmatch_array_size = 1024;

    h->sigs = xxmalloc(sizeof(struct sigs)*(maxsigma+1));
    h->has_flags = 0;
    h->flag_list = NULL;

    /* Malloc first array of trie and store trie ptrs to be able to free later */
    /* when apply_clear() is called.                                           */

    h->sigma_trie = xxcalloc(256,sizeof(struct sigma_trie));
    h->sigma_trie_arrays = xxmalloc(sizeof(struct sigma_trie_arrays));
    h->sigma_trie_arrays->arr = h->sigma_trie;
    h->sigma_trie_arrays->next = NULL;

    for (i=0;i<256;i++)
	(h->sigma_trie+i)->next = NULL;
    for (sig = h->gsigma; sig != NULL && sig->number != -1; sig = sig->next) {
	if (my_flag_check(sig->symbol)) {
	    h->has_flags = 1;
	    my_apply_add_flag(h, my_flag_get_name(sig->symbol));
	}
	(h->sigs+(sig->number))->symbol = sig->symbol;
	(h->sigs+(sig->number))->length = strlen(sig->symbol);
	/* Add sigma entry to trie */
	if (sig->number > IDENTITY) {
	    my_apply_add_sigma_trie(h, sig->number, sig->symbol, (h->sigs+(sig->number))->length);
	}
    }
    if (maxsigma >= IDENTITY) {
	(h->sigs+EPSILON)->symbol = h->epsilon_symbol;
	(h->sigs+EPSILON)->length =  strlen(h->epsilon_symbol);
	(h->sigs+UNKNOWN)->symbol = "?";
	(h->sigs+UNKNOWN)->length =  1;
	(h->sigs+IDENTITY)->symbol = "@";
	(h->sigs+IDENTITY)->length =  1;
    }
    if (h->has_flags) {

	h->flag_lookup = xxmalloc(sizeof(struct flag_lookup)*(maxsigma+1));
	for (i=0; i <= maxsigma; i++) {
	    (h->flag_lookup+i)->type = 0;
	    (h->flag_lookup+i)->name = NULL;
	    (h->flag_lookup+i)->value = NULL;
	}
	for (sig = h->gsigma; sig != NULL ; sig = sig->next) {
	    if (my_flag_check(sig->symbol)) {
		(h->flag_lookup+sig->number)->type = my_flag_get_type(sig->symbol);
		(h->flag_lookup+sig->number)->name = my_flag_get_name(sig->symbol);
		(h->flag_lookup+sig->number)->value = my_flag_get_value(sig->symbol);
	    }
	}
	my_apply_mark_flagstates(h);
    }
}

struct apply_handle *
my_apply_init(struct fsm *net) {
    struct apply_handle *h;

    srand((unsigned int) time(NULL));
    h = calloc(1,sizeof(struct apply_handle));
    /* Init */

    h->iterate_old = 0;
    h->iterator = 0;
    h->instring = NULL;
    h->flag_list = NULL;
    h->flag_lookup = NULL;
    h->obey_flags = 1;
    h->show_flags = 0;
    h->print_space = 0;
    h->print_pairs = 0;
    h->separator = xxstrdup(":");
    h->epsilon_symbol = xxstrdup("0");
    h->last_net = net;
    h->outstring = xxmalloc(sizeof(char)*DEFAULT_OUTSTRING_SIZE);
    h->outstringtop = DEFAULT_OUTSTRING_SIZE;
    *(h->outstring) = '\0';
    h->gstates = net->states;
    h->gsigma = net->sigma;
    h->printcount = 1;
    my_apply_create_statemap(h, net);
    h->searchstack = xxmalloc(sizeof(struct searchstack) * DEFAULT_STACK_SIZE);
    h->apply_stack_top = DEFAULT_STACK_SIZE;
    my_apply_stack_clear(h);
    my_apply_create_sigarray(h, net);
    return(h);
}

void
my_apply_set_print_space(struct apply_handle *h, int value) {
    h->print_space = value;
    h->space_symbol = xxstrdup(" ");
}

void
my_apply_set_print_pairs(struct apply_handle *h, int value) {
    h->print_pairs = value;
}

void
my_apply_set_show_flags(struct apply_handle *h, int value) {
    h->show_flags = value;
}

void
my_apply_set_obey_flags(struct apply_handle *h, int value) {
    h->obey_flags = value;
}

void
my_iface_apply_set_params(struct apply_handle *h) {
    // !!!clb!!! was global
    int g_print_space = 0; 
    int g_print_pairs = 0; 
    int g_show_flags = 0; 
    int g_obey_flags = 1; 

    my_apply_set_print_space(h, g_print_space);
    my_apply_set_print_pairs(h, g_print_pairs);
    my_apply_set_show_flags(h, g_show_flags);
    my_apply_set_obey_flags(h, g_obey_flags);
}

void
my_iface_apply_up(char *word, struct fsm *fsm) {
    int i;
    char *result;
    struct apply_handle *ah;

    // !!!clb!!!
    //ah = stack_get_ah();
    //if (se->ah == NULL) {
    //	se->ah = apply_init(se->fsm);
    //}
    ah = my_apply_init(fsm);

    my_iface_apply_set_params(ah);
/*    result = apply_up(ah, word);

    if (result == NULL) {
        printf("???\n");
        return;
    } else {
        printf("%s\n",result);
    }
    for (i = g_list_limit; i > 0; i--) {
        result = apply_up(ah, NULL);
        if (result == NULL)
            break;
        printf("%s\n",result);
    }
*/
}


int
main(void) {
    struct fsm *p_fsm;

    printf("start\n");

    p_fsm = my_iface_load_stack("morphind");
    my_fsm_destroy(p_fsm);

    printf("finish\n");
    return 0;
}
