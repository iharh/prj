#include <stdio.h>

unsigned char *charset_table_get();

int
main(int argc, char *argv[])
{
    char *mblen = (char *)charset_table_get();
    int v = mblen[1];

    printf("1-st byte value: %d\n", v);

    return 0;
}
