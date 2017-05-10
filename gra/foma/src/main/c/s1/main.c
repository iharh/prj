#include <stdio.h>

#include "smallrt.h"

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

struct fsm *
my_iface_load_stack(char *filename) {
    struct fsm *net;
    fsm_read_binary_handle fsrh;

    if ((fsrh = fsm_read_text_file_multiple_init(filename)) == NULL) {
	fprintf(stderr, "%s: ", filename);
        perror("File error");
        return;
    }
    net = my_fsm_read_binary_file_multiple(fsrh);
    //printf("loaded fsm\n");
    return net;
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
