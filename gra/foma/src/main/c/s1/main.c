#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <limits.h>
#include <time.h>
#include <memory.h>

//#include "smallrt.h"
// need this in order to correct invalid behaviour when str pointer has higher than 32-bits
char *strstr(const char *str, const char *strSearch);

void *xxmalloc(size_t size) {
    return(malloc(size));
}

char *xxstrndup(const char *s, size_t n) {
    char *r = NULL;
    const char *p = s;
    while(*p++ && n--);
    n = p - s - 1;
    r = (char *) xxmalloc(n + 1);
    if(r != NULL) {
        memcpy(r, s, n);
        r[n] = 0;
    }
    return r;
}

void xxfree(void *ptr) {
    free(ptr);
}

void *xxrealloc(void *ptr, size_t size) {
    return(realloc(ptr, size));
}

void *xxcalloc(size_t nmemb, size_t size) {
    return(calloc(nmemb,size));
}

char *xxstrdup(const char *s) {
  //    return(strdup(s));
  size_t size = strlen(s) + 1;
  char *p = malloc(size);
  if (p) {
    memcpy(p, s, size);
  }
  return p;
}

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

#define RANDOM 1
#define ENUMERATE 2
//#define MATCH 4
#define UP 8
#define DOWN 16
#define LOWER 32
#define UPPER 64
//#define SPACE 128

#define FAIL 0
#define SUCCEED 1

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

#if defined (_MSC_VER) || (__MINGW32__)
#define LONG_LONG_SPECIFIER "%I64d"
#else
#define LONG_LONG_SPECIFIER "%lld"
#endif

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
        new_symbol = strstr(buf, " "); // !!! need to declare strstr before !!!
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

#define APPLY_BINSEARCH_THRESHOLD 10

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

void
my_apply_stack_pop (struct apply_handle *h) {
    struct flag_list *flist;
    struct searchstack *ss;
    (h->apply_stack_ptr)--;
    ss = h->searchstack+h->apply_stack_ptr;

    h->iptr =  ss->iptr;
    h->ptr  =  ss->offset;
    h->ipos =  ss->ipos;
    h->opos =  ss->opos;
    h->state_has_index = ss->state_has_index;
    /* Restore mark */
    *(h->marks+(h->gstates+h->ptr)->state_no) = ss->visitmark;

    if (h->has_flags && ss->flagname != NULL) {
	/* Restore flag */
	for (flist = h->flag_list; flist != NULL; flist = flist->next) {
	    if (strcmp(flist->name, ss->flagname) == 0) {
		break;
	    }
	}
	if (flist == NULL)
	    perror("***Nothing to pop\n");
	flist->value = ss->flagvalue;
	flist->neg = ss->flagneg;
    }
}

int
my_apply_stack_isempty(struct apply_handle *h) {
    if (h->apply_stack_ptr == 0) {
	return 1;
    }
    return 0;
}

static void
my_apply_stack_push (struct apply_handle *h, int vmark, char *sflagname, char *sflagvalue, int sflagneg) {
    struct searchstack *ss;
    if (h->apply_stack_ptr == h->apply_stack_top) {
	h->searchstack = xxrealloc(h->searchstack, sizeof(struct searchstack)* ((h->apply_stack_top)*2));
	if (h->searchstack == NULL) {
	  perror("Apply stack full!!!\n");
	  exit(0);
	}
	h->apply_stack_top *= 2;
    }
    ss = h->searchstack+h->apply_stack_ptr;
    ss->offset     = h->curr_ptr;
    ss->ipos       = h->ipos;
    ss->opos       = h->opos;
    ss->visitmark  = vmark;
    ss->iptr       = h->iptr;
    ss->state_has_index = h->state_has_index;
    if (h->has_flags) {
	ss->flagname   = sflagname;
	ss->flagvalue  = sflagvalue;
	ss->flagneg    = sflagneg;
    }
    (h->apply_stack_ptr)++;
}

int
my_apply_append(struct apply_handle *h, int cptr, int sym) {

    char *astring, *bstring, *pstring;
    int symin, symout, len, alen, blen, idlen;
    
    symin = (h->gstates+cptr)->in;
    symout = (h->gstates+cptr)->out;
    astring = ((h->sigs)+symin)->symbol;
    alen =  ((h->sigs)+symin)->length;
    bstring = ((h->sigs)+symout)->symbol;
    blen =  ((h->sigs)+symout)->length;
    
    while (alen + blen + h->opos + 2 + strlen(h->separator) >= h->outstringtop) {
	//    while (alen + blen + h->opos + 3 >= h->outstringtop) {
	h->outstring = xxrealloc(h->outstring, sizeof(char) * ((h->outstringtop) * 2));
	(h->outstringtop) *= 2;
    }
    
    if ((h->has_flags) && !h->show_flags && (h->flag_lookup+symin)->type) {
	astring = ""; alen = 0;
    }
    if (h->has_flags && !h->show_flags && (h->flag_lookup+symout)->type) {
	bstring = ""; blen = 0;
    }
    if (((h->mode) & ENUMERATE) == ENUMERATE) {
	/* Print both sides separated by colon */
	/* if we're printing "words" */
	if (((h->mode) & (UPPER | LOWER)) == (UPPER|LOWER)) {
	    
	    if (astring == bstring) {
		strcpy(h->outstring+h->opos, astring);
		len = alen;
	    } else {
		strcpy(h->outstring+h->opos, astring);
		//		strcpy(h->outstring+h->opos+alen,":");
		strcpy(h->outstring+h->opos+alen,h->separator);
		//strcpy(h->outstring+h->opos+alen+1,bstring);
		strcpy(h->outstring+h->opos+alen+strlen(h->separator),bstring);
		//		len = alen+blen+1;
		len = alen+blen+strlen(h->separator);
	    }
	}
	
	/* Print one side only */
	if (((h->mode) & (UPPER|LOWER)) != (UPPER|LOWER)) {
	    
	    if (symin == EPSILON) {
		astring = ""; alen = 0;
	    }
	    if (symout == EPSILON) {
		bstring = ""; blen = 0;
	    }
	    if (((h->mode) & (UPPER|LOWER)) == UPPER) {
		pstring = astring;
		len = alen;
	    } else {
		pstring = bstring;
		len = blen;
	    }
	    //strcpy(h->outstring+h->opos, pstring);
	    memcpy(h->outstring+h->opos, pstring, len);
	}
    }
    if (((h->mode) & ENUMERATE) != ENUMERATE) {
	/* Print pairs is ON and symbols are different */
	if (h->print_pairs && (symin != symout)) {

	    if (symin == UNKNOWN && ((h->mode) & DOWN) == DOWN)
		strncpy(astring, h->instring+h->ipos, 1);
	    if (symout == UNKNOWN && ((h->mode) & UP) == UP)
		strncpy(bstring, h->instring+h->ipos, 1);
	    strcpy(h->outstring+h->opos, "<");
	    strcpy(h->outstring+h->opos+1, astring);
	    //strcpy(h->outstring+h->opos+alen+1,":");
	    strcpy(h->outstring+h->opos+alen+1,h->separator);
	    //strcpy(h->outstring+h->opos+alen+2,bstring);
	    strcpy(h->outstring+h->opos+alen+1+strlen(h->separator), bstring);
	    //strcpy(h->outstring+h->opos+alen+blen+2,">");
	    strcpy(h->outstring+h->opos+alen+blen+1+strlen(h->separator),">");
	    //len = alen+blen+3;
	    len = alen+blen+2+strlen(h->separator);
	}

	else if (sym == IDENTITY) {
	    /* Apply up/down */
	    //idlen = utf8skip(h->instring+h->ipos)+1;
	    idlen = (h->sigmatch_array+h->ipos)->consumes; // here
	    strncpy(h->outstring+h->opos, h->instring+h->ipos, idlen);
	    strncpy(h->outstring+h->opos+idlen,"", 1);
	    len = idlen;
	} else if (sym == EPSILON) {
	    return(0);
	} else {
	    if (((h->mode) & DOWN) == DOWN) {
		pstring = bstring;
		len = blen;
	    } else {
		pstring = astring;
		len = alen;
	    }
	    memcpy(h->outstring+h->opos, pstring, len);
	}
    }
    if (h->print_space && len > 0) {
	strcpy(h->outstring+h->opos+len, h->space_symbol);
	len++;
    }
    return(len);
}

int
my_apply_match_length(struct apply_handle *h, int symbol) {
    if (symbol == EPSILON) {
	return 0;
    }
    if (h->has_flags && (h->flag_lookup+symbol)->type) {
	return 0;
    }
    if (((h->mode) & ENUMERATE) == ENUMERATE) {
	return 0;
    }
    if (h->ipos >= h->current_instring_length) {
	return -1;
    }
    if ((h->sigmatch_array+(h->ipos))->signumber == symbol) {
	    return((h->sigmatch_array+(h->ipos))->consumes);
    }
    if ((symbol == IDENTITY) || (symbol == UNKNOWN)) {
	if ((h->sigmatch_array+h->ipos)->signumber == IDENTITY) {
	    return((h->sigmatch_array+(h->ipos))->consumes);
	}
    }
    return -1;
}

/* Check for flag consistency by looking at the current states of */
/* flags in flist */

int
my_apply_check_flag(struct apply_handle *h, int type, char *name, char *value) {
    struct flag_list *flist, *flist2;
    for (flist = h->flag_list; flist != NULL; flist = flist->next) {
	if (strcmp(flist->name, name) == 0) {
	    break;
	}
    }
    h->oldflagvalue = flist->value;
    h->oldflagneg = flist->neg;
    
    if (type == FLAG_UNIFY) {
	if (flist->value == NULL) {
	    flist->value = xxstrdup(value);
	    return SUCCEED;
	}
	else if (strcmp(value, flist->value) == 0 && flist->neg == 0) {
	    return SUCCEED;
	} else if (strcmp(value, flist->value) != 0 && flist->neg == 1) {
	    flist->value = xxstrdup(value);
	    flist->neg = 0;
	    return SUCCEED;
	}
	return FAIL;
    }

    if (type == FLAG_CLEAR) {
	flist->value = NULL;
	flist->neg = 0;
	return SUCCEED;
    }

    if (type == FLAG_DISALLOW) {
	if (flist->value == NULL) {
	    return SUCCEED;
	}
	if (value == NULL && flist->value != NULL) {
	    return FAIL;
	}
	if (strcmp(value, flist->value) != 0) {
            if (flist->neg == 1)
                return FAIL;
            return SUCCEED;
	}
	if (strcmp(value, flist->value) == 0 && flist->neg == 1) {
            return SUCCEED;
        }
        return FAIL;
    }

    if (type == FLAG_NEGATIVE) {
	flist->value = value;
	flist->neg = 1;
	return SUCCEED;
    }

    if (type == FLAG_POSITIVE) {
	flist->value = value;
	flist->neg = 0;
	return SUCCEED;
    }

    if (type == FLAG_REQUIRE) {

	if (value == NULL) {
	    if (flist->value == NULL) {
		return FAIL;
	    } else {
		return SUCCEED;
	    }
	} else {
	    if (flist->value == NULL) {
		return FAIL;
	    }
	    if (strcmp(value, flist->value) != 0) {
		return FAIL;
	    } else {
                if (flist->neg == 1) {
                    return FAIL;
                }
		return SUCCEED;
	    }
	}
    }

    if (type == FLAG_EQUAL) {
	for (flist2 = h->flag_list; flist2 != NULL; flist2 = flist2->next) {
	    if (strcmp(flist2->name, value) == 0) {
		break;
	    }
	}

	if (flist2 == NULL && flist->value != NULL)
	    return FAIL;
	if (flist2 == NULL && flist->value == NULL) {
	    return SUCCEED;
	}
	if (flist2->value == NULL || flist->value == NULL) {
	    if (flist2->value == NULL && flist->value == NULL && flist->neg == flist2->neg) {
		return SUCCEED;
	    } else {
		return FAIL;
	    }
	}  else if (strcmp(flist2->value, flist->value) == 0 && flist->neg == flist2->neg) {
	    return SUCCEED;
	}
	return FAIL;
    }
    fprintf(stderr,"***Don't know what do with flag [%i][%s][%s]\n", type, name, value);
    return FAIL;
}

/* Match a symbol from sigma against the current position in string */
/* Return the number of symbols consumed in input string            */
/* For flags, we consume 0 symbols of the input string, naturally   */

int
my_apply_match_str(struct apply_handle *h, int symbol, int position) {
    if (((h->mode) & ENUMERATE) == ENUMERATE) {
	if (h->has_flags && (h->flag_lookup+symbol)->type) {
	    if (!h->obey_flags) {
		return 0;
	    }
	    if (my_apply_check_flag(h,(h->flag_lookup+symbol)->type, (h->flag_lookup+symbol)->name, (h->flag_lookup+symbol)->value) == SUCCEED) {
		return 0;
	    } else {
		return -1;
	    }
	}
	return(0);
    }


    if (symbol == EPSILON) {
	return 0;
    }
    
    /* If symbol is a flag, we need to check consistency */
    if (h->has_flags && (h->flag_lookup+symbol)->type) {
	if (!h->obey_flags) {
	    return 0;
	}
	if (my_apply_check_flag(h,(h->flag_lookup+symbol)->type, (h->flag_lookup+symbol)->name, (h->flag_lookup+symbol)->value) == SUCCEED) {
	    return 0;
	} else {
	    return -1;
	}
    }
    
    if (position >= h->current_instring_length) {
	return -1;
    }
    if ((h->sigmatch_array+position)->signumber == symbol) {
	return((h->sigmatch_array+position)->consumes);
    }
    if ((symbol == IDENTITY) || (symbol == UNKNOWN)) {
	if ((h->sigmatch_array+position)->signumber == IDENTITY) {
	    return((h->sigmatch_array+position)->consumes);
	}
    }
    return -1;
}

int
my_apply_binarysearch(struct apply_handle *h) {
    int thisstate, nextsym, seeksym, thisptr, lastptr, midptr;

    thisptr = h->curr_ptr = h->ptr;
    nextsym  = (((h->mode) & DOWN) == DOWN) ? (h->gstates+h->curr_ptr)->in  : (h->gstates+h->curr_ptr)->out;
    if (nextsym == EPSILON)
	return 1;
    if (nextsym == -1)
	return 0;
    if (h->ipos >= h->current_instring_length) {
	return 0;
    }
    seeksym = (h->sigmatch_array+h->ipos)->signumber;
    if (seeksym == nextsym || (nextsym == UNKNOWN && seeksym == IDENTITY))
	return 1;

    thisstate = (h->gstates+thisptr)->state_no;
    lastptr = *(h->statemap+thisstate)+*(h->numlines+thisstate)-1;
    thisptr++;

    if (seeksym == IDENTITY || lastptr - thisptr < APPLY_BINSEARCH_THRESHOLD) {
	for ( ; thisptr <= lastptr; thisptr++) {
	    nextsym = (((h->mode) & DOWN) == DOWN) ? (h->gstates+thisptr)->in : (h->gstates+thisptr)->out;
	    if ((nextsym == seeksym) || (nextsym == UNKNOWN && seeksym == IDENTITY)) {
		h->curr_ptr = thisptr;
		return 1;
	    }
	    if (nextsym > seeksym || nextsym == -1) {
		return 0;
	    }
	}
	return 0;
    }
     
    for (;;)  {
	if (thisptr > lastptr) { return 0; }
	midptr = (thisptr+lastptr)/2;
	nextsym = (((h->mode) & DOWN) == DOWN) ? (h->gstates+midptr)->in : (h->gstates+midptr)->out;
	if (seeksym < nextsym) {
	    lastptr = midptr - 1;
	    continue;
	} else if (seeksym > nextsym) {
	    thisptr = midptr + 1;
	    continue;
	} else {

	    while (((((h->mode) & DOWN) == DOWN) ? (h->gstates+(midptr-1))->in : (h->gstates+(midptr-1))->out) == seeksym) {
		midptr--; /* Find first match in case of ties */
	    }
	    h->curr_ptr = midptr;
	    return 1;
	}
    }
}

/* map h->ptr (line pointer) to h->iptr (index pointer) */
void
my_apply_set_iptr(struct apply_handle *h) {
    struct apply_state_index **idx, *sidx;
    int stateno, seeksym;
    /* Check if state has index */
    if ((idx = ((h->mode) & DOWN) == DOWN ? (h->index_in) : (h->index_out)) == NULL) {
	return;
    }
 
    h->iptr = NULL;
    h->state_has_index = 0;
    stateno = (h->gstates+h->ptr)->state_no;
    if (stateno < 0) {
	return;
    }
   
    sidx = *(idx + stateno);
    if (sidx == NULL) { return; }
    seeksym = (h->sigmatch_array+h->ipos)->signumber;
    h->state_has_index = 1;
    sidx = sidx + seeksym;
    if (sidx->fsmptr == -1) {
	if (sidx->next == NULL) {
	    return;
	} else {
	    sidx = sidx->next;
	}
    }
    h->iptr = sidx;
    if (sidx->fsmptr == -1) {
	h->iptr = NULL;
    }
    h->state_has_index = 1;
}

int
my_apply_follow_next_arc(struct apply_handle *h) {
    char *fname, *fvalue;
    int eatupi, eatupo, symin, symout, fneg;
    int vcount, marksource, marktarget;
    
    /* Here we follow three possible search strategies:        */
    /* (1) if the state in question has an index, we use that  */
    /* (2) if the state is binary searchable, we use that      */
    /* (3) otherwise we traverse arc-by-arc                    */
    /*     Condition (2) needs arcs to be sorted in the proper */
    /*     direction, and requires that the state be flag-free */
    /*     For those states that aren't flag-free, (3) is used */

    if (h->state_has_index) {
	for ( ; h->iptr != NULL && h->iptr->fsmptr != -1; ) {

	    h->ptr = h->curr_ptr = h->iptr->fsmptr;
	    if (((h->mode) & DOWN) == DOWN) {
		symin = (h->gstates+h->curr_ptr)->in;
		symout = (h->gstates+h->curr_ptr)->out;
	    } else {
		symin = (h->gstates+h->curr_ptr)->out;
		symout = (h->gstates+h->curr_ptr)->in;
	    }
	    
	    marksource = *(h->marks+(h->gstates+h->ptr)->state_no);
	    marktarget = *(h->marks+(h->gstates+(*(h->statemap+(h->gstates+h->curr_ptr)->target)))->state_no);
	    eatupi = my_apply_match_length(h, symin);
	    if (!(eatupi == -1 || -1-(h->ipos)-eatupi == marktarget)) {     /* input 2x EPSILON loop check */
		if ((eatupi = my_apply_match_str(h, symin, h->ipos)) != -1) {
		    eatupo = my_apply_append(h, h->curr_ptr, symout);
		    if (h->obey_flags && h->has_flags && ((h->flag_lookup+symin)->type & (FLAG_UNIFY|FLAG_CLEAR|FLAG_POSITIVE|FLAG_NEGATIVE))) {
			fname = (h->flag_lookup+symin)->name;
			fvalue = h->oldflagvalue;
			fneg = h->oldflagneg;
		    } else {
			fname = fvalue = NULL;
			fneg = 0;
		    }
		    /* Push old position */
		    my_apply_stack_push(h, marksource, fname, fvalue, fneg);
		    h->ptr = *(h->statemap+(h->gstates+h->curr_ptr)->target);
		    h->ipos += eatupi;
		    h->opos += eatupo;
		    my_apply_set_iptr(h);
		    return 1;
		}
	    }
	    h->iptr = h->iptr->next;
	}
	return 0;
    } else if ((h->binsearch && !(h->has_flags)) || (h->binsearch && !(BITTEST(h->flagstates, (h->gstates+h->ptr)->state_no)))) {
	for (;;) {
	    if (my_apply_binarysearch(h)) {
		if (((h->mode) & DOWN) == DOWN) {
		    symin = (h->gstates+h->curr_ptr)->in;
		    symout = (h->gstates+h->curr_ptr)->out;
		} else {
		    symin = (h->gstates+h->curr_ptr)->out;
		    symout = (h->gstates+h->curr_ptr)->in;
		}
		
		marksource = *(h->marks+(h->gstates+h->ptr)->state_no);
		marktarget = *(h->marks+(h->gstates+(*(h->statemap+(h->gstates+h->curr_ptr)->target)))->state_no);
		
		eatupi = my_apply_match_length(h, symin);
		if (eatupi != -1 && -1-(h->ipos)-eatupi != marktarget) {
		    if ((eatupi = my_apply_match_str(h, symin, h->ipos)) != -1) {
			eatupo = my_apply_append(h, h->curr_ptr, symout);
			
			/* Push old position */
			my_apply_stack_push(h, marksource, NULL, NULL, 0);
			
			/* Follow arc */
			h->ptr = *(h->statemap+(h->gstates+h->curr_ptr)->target);
			h->ipos += eatupi;
			h->opos += eatupo;
			my_apply_set_iptr(h);
			return 1;
		    }
		}
		if ((h->gstates+h->curr_ptr)->state_no == (h->gstates+h->curr_ptr+1)->state_no) {
		    h->curr_ptr++;
		    h->ptr = h->curr_ptr;
		    if ((h->gstates+h->curr_ptr)-> target == -1) {
			return 0;
		    }
		    continue;
		}
	    }
	    return 0;
	}
    } else {
	for (h->curr_ptr = h->ptr; (h->gstates+h->curr_ptr)->state_no == (h->gstates+h->ptr)->state_no && (h->gstates+h->curr_ptr)-> in != -1; (h->curr_ptr)++) {
	    
	    /* Select one random arc to follow out of all outgoing arcs */
	    if ((h->mode & RANDOM) == RANDOM) {
		vcount = 0;
		for (h->curr_ptr = h->ptr;  (h->gstates+h->curr_ptr)->state_no == (h->gstates+h->ptr)->state_no && (h->gstates+h->curr_ptr)-> in != -1; (h->curr_ptr)++) {
		    vcount++;
		}
		if (vcount > 0) {
		    h->curr_ptr = h->ptr + (rand() % vcount);
		} else {
		    h->curr_ptr = h->ptr;
		}
	    }
	    
	    if (((h->mode) & DOWN) == DOWN) {
		symin = (h->gstates+h->curr_ptr)->in;
		symout = (h->gstates+h->curr_ptr)->out;
	    } else {
		symin = (h->gstates+h->curr_ptr)->out;
		symout = (h->gstates+h->curr_ptr)->in;
	    }
	    
	    marksource = *(h->marks+(h->gstates+h->ptr)->state_no);
	    marktarget = *(h->marks+(h->gstates+(*(h->statemap+(h->gstates+h->curr_ptr)->target)))->state_no);

	    eatupi = my_apply_match_length(h, symin);

	    if (eatupi == -1 || -1-(h->ipos)-eatupi == marktarget) { continue; } /* loop check */
	    if ((eatupi = my_apply_match_str(h, symin, h->ipos)) != -1) {
		eatupo = my_apply_append(h, h->curr_ptr, symout);
		if (h->obey_flags && h->has_flags && ((h->flag_lookup+symin)->type & (FLAG_UNIFY|FLAG_CLEAR|FLAG_POSITIVE|FLAG_NEGATIVE))) {
		    
		    fname = (h->flag_lookup+symin)->name;
		    fvalue = h->oldflagvalue;
		    fneg = h->oldflagneg;
		} else {
		    fname = fvalue = NULL;
		    fneg = 0;
		}
		
		/* Push old position */
		my_apply_stack_push(h, marksource, fname, fvalue, fneg);
		
		/* Follow arc */
		h->ptr = *(h->statemap+(h->gstates+h->curr_ptr)->target);
		h->ipos += eatupi;
		h->opos += eatupo;
		my_apply_set_iptr(h);
		return(1);
	    }
	}
	return(0);
    }
}

char *
my_apply_return_string(struct apply_handle *h) {
    /* Stick a 0 to endpos to avoid getting old accumulated gunk strings printed */
    *(h->outstring+h->opos) = '\0';
    if (((h->mode) & RANDOM) == RANDOM) {
	/* To end or not to end */
	if (!(rand() % 2)) {
	    my_apply_stack_clear(h);
	    h->iterator = 0;
	    h->iterate_old = 0;
	    return(h->outstring);
	}
    } else {
	return(h->outstring);
    }
    return(NULL);
}

static void
my_apply_force_clear_stack(struct apply_handle *h) {
    /* Make sure stack is empty and marks reset */
    if (!my_apply_stack_isempty(h)) {
	*(h->marks+(h->gstates+h->ptr)->state_no) = 0;
	while (!my_apply_stack_isempty(h)) {
	    my_apply_stack_pop(h);
	    *(h->marks+(h->gstates+h->ptr)->state_no) = 0;
	}
	h->iterator = 0;
	h->iterate_old = 0;
	my_apply_stack_clear(h);
    }
}

void
my_apply_clear_flags(struct apply_handle *h) {
    struct flag_list *flist;
    for (flist = h->flag_list; flist != NULL; flist = flist->next) {
	flist->value = NULL;
	flist->neg = 0;
    }
    return;
}

void
my_apply_mark_state(struct apply_handle *h) {

    /* This controls the how epsilon-loops are traversed.  Such loops can    */
    /* only be followed once to reach a state already visited in the DFS.    */
    /* This requires that we store the number of input symbols consumed      */
    /* whenever we enter a new state.  If we enter the same state twice      */
    /* with the same number of input symbols consumed, we abandon the search */
    /* for that branch. Flags are epsilons from this point of view.          */
    /* The encoding of h->marks is:                                          */
    /* 0 = unseen, +ipos = seen at ipos, -ipos = seen second time at ipos    */

    if ((h->mode & RANDOM) != RANDOM) {
	if (*(h->marks+(h->gstates+h->ptr)->state_no) == h->ipos+1) {
	    *(h->marks+(h->gstates+h->ptr)->state_no) = -(h->ipos+1);
	} else {
	    *(h->marks+(h->gstates+h->ptr)->state_no) = h->ipos+1;
	}
    }
}

void
my_apply_skip_this_arc(struct apply_handle *h) {
    /* If we have index ptr */
    if (h->iptr) {
	h->ptr = h->iptr->fsmptr;
	h->iptr = h->iptr->next;
	/* Otherwise */
    } else {
	(h->ptr)++;
    }
}

int
my_apply_at_last_arc(struct apply_handle *h) {
    int seeksym, nextsym;
    if (h->state_has_index) {
	if (h->iptr->next == NULL || h->iptr->next->fsmptr == -1) {
	    return 1;
	}
    } else {
	if  ((h->binsearch && !(h->has_flags)) || (h->binsearch && !(BITTEST(h->flagstates, (h->gstates+h->ptr)->state_no)))) {
	    if ((h->gstates+h->ptr)->state_no != (h->gstates+h->ptr+1)->state_no) {
		return 1;
	    }
	    seeksym = (h->sigmatch_array+h->ipos)->signumber;
	    nextsym  = (((h->mode) & DOWN) == DOWN) ? (h->gstates+h->ptr)->in  : (h->gstates+h->ptr)->out;
	    if (nextsym == -1 || seeksym < nextsym) {
		return 1;
	    }
	} else {
	    if ((h->gstates+h->ptr)->state_no != (h->gstates+h->ptr+1)->state_no) {
		return 1;
	    }
	}
    }
    return 0;
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

/* Checks if the next character in the string is a combining character     */
/* according to Unicode 7.0                                                */
/* i.e. codepoints 0300-036F  Combining Diacritical Marks                  */
/*                 1AB0-1ABE  Combining Diacritical Marks Extended         */
/*                 1DC0-1DFF  Combining Diacritical Marks Supplement       */
/*                 20D0-20F0  Combining Diacritical Marks for Symbols      */
/*                 FE20-FE2D  Combining Half Marks                         */
/* Returns number of bytes of char. representation, or 0 if not combining  */
int
my_utf8iscombining(unsigned char *s) {
    if (*s == '\0' || *(s+1) == '\0')
	return 0;
    if (!(*s == 0xcc || *s == 0xcd || *s == 0xe1 || *s == 0xe2 || *s == 0xef))
	return 0;
    /* 0300-036F */
    if (*s == 0xcc && *(s+1) >= 0x80 && *(s+1) <= 0xbf)
	return 2;
    if (*s == 0xcd && *(s+1) >= 0x80 && *(s+1) <= 0xaf)
	return 2;
    if (*(s+2) == '\0')
	return 0;
    /* 1AB0-1ABE */
    if (*s == 0xe1 && *(s+1) == 0xaa && *(s+2) >= 0xb0 && *(s+2) <= 0xbe)
	return 3;
    /* 1DC0-1DFF */
    if (*s == 0xe1 && *(s+1) == 0xb7 && *(s+2) >= 0x80 && *(s+2) <= 0xbf)
	return 3;
    /* 20D0-20F0 */
    if (*s == 0xe2 && *(s+1) == 0x83 && *(s+2) >= 0x90 && *(s+2) <= 0xb0)
	return 3;
    /* FE20-FE2D */
    if (*s == 0xef && *(s+1) == 0xb8 && *(s+2) >= 0xa0 && *(s+2) <= 0xad)
	return 3;
    return 0;
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
my_apply_clear_index_list(struct apply_handle *h, struct apply_state_index **index) {
    int i, j, statecount;
    struct apply_state_index *iptr, *iptr_tmp, *iptr_zero;
    if (index == NULL)
	return;
    statecount = h->last_net->statecount;
    for (i = 0; i < statecount; i++) {
	iptr = *(index+i);
	if (iptr == NULL) {
	    continue;
	}
	iptr_zero = *(index+i);
	for (j = h->sigma_size - 1 ; j >= 0; j--) { /* Make sure to not free the list in EPSILON    */
	    iptr = *(index+i) + j;                  /* as the other states lists' tails point to it */
	    for (iptr = iptr->next ; iptr != NULL && iptr != iptr_zero; iptr = iptr_tmp) {
		iptr_tmp = iptr->next;
		xxfree(iptr);
	    }
	}
	xxfree(*(index+i));
    }
}

void
my_apply_clear_index(struct apply_handle *h) {
    if (h->index_in) {
	my_apply_clear_index_list(h, h->index_in);
	xxfree(h->index_in);
	h->index_in = NULL;
    }
    if (h->index_out) {
	my_apply_clear_index_list(h, h->index_out);
	xxfree(h->index_out);
	h->index_out = NULL;
    }
}

/* Frees memory associated with applies */
void
my_apply_clear(struct apply_handle *h) {
    struct sigma_trie_arrays *sta, *stap;
    for (sta = h->sigma_trie_arrays; sta != NULL; ) {
	stap = sta;
	xxfree(sta->arr);
	sta = sta->next;
	xxfree(stap);
    }
    h->sigma_trie_arrays = NULL;
    if (h->statemap != NULL) {
        xxfree(h->statemap);
        h->statemap = NULL;
    }
    if (h->numlines != NULL) {
        xxfree(h->numlines);
        h->numlines = NULL;
    }
    if (h->marks != NULL) {
        xxfree(h->marks);
        h->marks = NULL;
    }
    if (h->searchstack != NULL) {
        xxfree(h->searchstack);
        h->searchstack = NULL;
    }
    if (h->sigs != NULL) {
        xxfree(h->sigs);
        h->sigs = NULL;
    }
    if (h->flag_lookup != NULL) {
        xxfree(h->flag_lookup);
        h->flag_lookup = NULL;
    }
    if (h->sigmatch_array != NULL) {
	xxfree(h->sigmatch_array);
	h->sigmatch_array = NULL;
    }
    if (h->flagstates != NULL) {
	xxfree(h->flagstates);
	h->flagstates = NULL;
    }    
    my_apply_clear_index(h);
    h->last_net = NULL;
    h->iterator = 0;
    xxfree(h->outstring);
    xxfree(h->separator);
    xxfree(h->epsilon_symbol);
    xxfree(h);
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

char *
my_apply_net(struct apply_handle *h) {

/*     We perform a basic DFS on the graph, with two minor complications:       */

/*     1. We keep a mark for each state which indicates how many input symbols  */
/*        we had consumed the last time we entered that state on the current    */
/*        "run."  If we reach a state seen twice without consuming input, we    */
/*        terminate that branch of the search.                                  */
/*        As we pop a position, we also unmark the state we came from.          */
 
/*     2. If the graph has flags, we push the previous flag value when          */
/*        traversing a flag-modifying arc (P,U,N, or C).  This is because a     */
/*        flag may have been set during the previous "run" and may not apply.   */
/*        Since we're doing a DFS, we can be sure to return to the previous     */
/*        global flag state by just remembering that last flag change.          */

/*     3. The whole system needs to work as an iterator, meaning we need to     */
/*        store the global state of the search so we can resume it later to     */
/*        to yield more possible output words with the same input string.       */

    char *returnstring;

    if (h->iterate_old == 1) {     /* If called with NULL as the input word, this will be set */
        goto resume;
    }

    h->iptr = NULL; h->ptr = 0; h->ipos = 0; h->opos = 0;
    my_apply_set_iptr(h);

    my_apply_stack_clear(h);

    if (h->has_flags) {
	my_apply_clear_flags(h);
    }
    
    /* "The use of four-letter words like goto can occasionally be justified */
    /*  even in the best of company." Knuth (1974).                          */

    goto L2;

    while(!my_apply_stack_isempty(h)) {
	my_apply_stack_pop(h);
	/* If last line was popped */
	if (my_apply_at_last_arc(h)) {
	    *(h->marks+(h->gstates+h->ptr)->state_no) = 0; /* Unmark   */
	    continue;                                      /* pop next */
	}
	my_apply_skip_this_arc(h);                         /* skip old pushed arc */
    L1:
	if (!my_apply_follow_next_arc(h)) {
	    *(h->marks+(h->gstates+h->ptr)->state_no) = 0; /* Unmark   */
	    continue;                                      /* pop next */
	}
    L2:
	/* Print accumulated string upon entry to state */
	if ((h->gstates+h->ptr)->final_state == 1 && (h->ipos == h->current_instring_length || ((h->mode) & ENUMERATE) == ENUMERATE)) {
	    if ((returnstring = (my_apply_return_string(h))) != NULL) {
		return(returnstring);
	    }
	}

    resume:
       	my_apply_mark_state(h);  /* Mark upon arrival to new state */
	goto L1;
    }
    if ((h->mode & RANDOM) == RANDOM) {
          my_apply_stack_clear(h);
          h->iterator = 0;
          h->iterate_old = 0;
          return(h->outstring);
    }
    my_apply_stack_clear(h);
    return NULL;
}

/* We need to know which symbols in sigma we can match for all positions           */
/* in the input string.  Alternatively, if there is no input string as is the case */
/* when we just list the words or randomly search the graph, we can match          */
/* any symbol in sigma.                                                            */

/* We create an array that for each position in the input string        */
/* has information on which symbol we can match at that position        */
/* as well as how many symbols matching consumes                        */

void
my_apply_create_sigmatch(struct apply_handle *h) {
    char *symbol;
    struct sigma_trie *st;
    int i, j, inlen, lastmatch, consumes, cons;
    /* We create a sigmatch array only in case we match against a real string */
    if (((h->mode) & ENUMERATE) == ENUMERATE) {
	return;
    }
    symbol = h->instring;
    inlen = strlen(symbol);
    h->current_instring_length = inlen;
    if (inlen >= h->sigmatch_array_size) {
	xxfree(h->sigmatch_array);
	h->sigmatch_array = xxmalloc(sizeof(struct sigmatch_array)*(inlen));
	h->sigmatch_array_size = inlen;
    }
    /* Find longest match in alphabet at current position */
    /* by traversing the trie of alphabet symbols         */
    for (i=0; i < inlen; i += consumes ) {
	st = h->sigma_trie;
	for (j=0, lastmatch = 0; ; j++) {
	    if (*(symbol+i+j) == '\0') {
		break;
	    }
	    st = st+(unsigned char)*(symbol+i+j);
	    if (st->signum != 0) {
		lastmatch = st->signum;
		if (st->next == NULL)
		    break;
		st = st->next;
	    } else if (st->next != NULL) {
		st = st->next;
	    } else {
		break;
	    }
	}
	if (lastmatch != 0) {
	    (h->sigmatch_array+i)->signumber = lastmatch;
	    consumes = (h->sigs+lastmatch)->length;
	} else {
	    /* Not found in trie */
	    (h->sigmatch_array+i)->signumber = IDENTITY;
	    consumes = my_utf8skip(symbol+i)+1;
	}

	/* If we now find trailing unicode combining characters (0300-036F):      */
	/* (1) Merge them all with current symbol                                 */
	/* (2) Declare the whole sequence one ? (IDENTITY) symbol                 */
        /*     Step 2 is motivated by the fact that                               */
	/*     if the input is S(symbol) + D(diacritic)                           */
        /*     and SD were a symbol in the alphabet, then this would have been    */
        /*     found when searching the alphabet symbols earlier, so SD+ => ?     */
        /*     Note that this means that a multi-char symbol followed by a        */
        /*     diacritic gets converted to a single ?, e.g.                       */
        /*     [TAG] + D => ? if [TAG] is in the alphabet, but [TAG]+D isn't.     */

	for (  ; (cons = my_utf8iscombining((unsigned char *)(symbol+i+consumes))); consumes += cons) {
	    (h->sigmatch_array+i)->signumber = IDENTITY;
	}
	(h->sigmatch_array+i)->consumes = consumes;
    }
}

char *
my_apply_updown(struct apply_handle *h, char *word) {

    char *result = NULL;

    if (h->last_net == NULL || h->last_net->finalcount == 0)
        return (NULL);
    if (word == NULL) {
        h->iterate_old = 1;
        result = my_apply_net(h);
    }
    else if (word != NULL) {
        h->iterate_old = 0;
        h->instring = word;
        my_apply_create_sigmatch(h);
	/* Remove old marks if necessary TODO: only pop marks */
	my_apply_force_clear_stack(h);
        result = my_apply_net(h);
    }
    return(result);
}

char *
my_apply_up(struct apply_handle *h, char *word) {

    h->mode = UP;
    if (h->index_out) {
	h->indexed = 1;
    } else {
	h->indexed = 0;
    }
    h->binsearch = (h->last_net->arcs_sorted_out == 1) ? 1 : 0;
    return(my_apply_updown(h, word));
}

void
my_iface_apply_up(char *word, struct fsm *fsm, struct apply_handle *ah) {
    int g_list_limit = 100;
    int i;
    char *result;

    result = my_apply_up(ah, word); // returns always h->outstring or NULL

    if (result == NULL) {
        printf("???\n");
        return;
    } else {
        printf("%s\n",result);
    }

    for (i = g_list_limit; i > 0; i--) {
        result = my_apply_up(ah, NULL);
        if (result == NULL)
            break;
        printf("%s\n",result);
    }
}

int
main(void) {
    struct fsm *p_fsm;
    struct apply_handle *p_ah;

    printf("start\n");

    p_fsm = my_iface_load_stack("morphind");

    p_ah = my_apply_init(p_fsm);
    my_iface_apply_set_params(p_ah);

    my_iface_apply_up("kirim", p_fsm, p_ah);
    my_iface_apply_up("aku", p_fsm, p_ah);

    my_apply_clear(p_ah);
    my_fsm_destroy(p_fsm);

    printf("finish\n");
    return 0;
}
