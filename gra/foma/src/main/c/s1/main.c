#include <stdio.h>

#include "foma.h"

void iface_load_stack(char *filename) {
    struct fsm *net;
    fsm_read_binary_handle fsrh;

    if ((fsrh = fsm_read_binary_file_multiple_init(filename)) == NULL) {
	fprintf(stderr, "%s: ", filename);
        perror("File error");
        return;
    }
    while ((net = fsm_read_binary_file_multiple(fsrh)) != NULL) {
        printf("loaded fsm\n");
        //stack_add(net);
    }
    return;
}


int
main(void) {
    printf("start\n");
    iface_load_stack("d:\\dev\prj\\gra\\foma\\morphind.bin");
    printf("finish\n");
    return 0;
}
