#include <stdio.h>

#ifdef __cplusplus
extern "C" {
#endif

unsigned char *charset_table_get();

#ifdef __cplusplus
}
#endif

int
main(int argc, char *argv[])
{
    char *mblen = (char *)charset_table_get();
    int v = mblen[1];

    printf("1-st byte value: %d\n", v);

    return 0;
}
