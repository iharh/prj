#include <stdio.h>

#include "foma.h"

struct my_test_struct {
    char *p1;
    char *p2;
};

int
main(void) {
    printf("start\n");

    struct my_test_struct *pTS;
    printf("pTS1: %p\n", pTS);
    pTS = (struct my_test_struct *)xxmalloc(sizeof(struct my_test_struct)); // xxmalloc
    if (pTS == NULL) {
        printf("pTS2: NULL\n");
    }
    printf("pTS2: %p\n", (size_t)pTS);
    (pTS->p1) = 0; // NULL

    printf("finish\n");
    return 0;
}
