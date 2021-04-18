#include <stdio.h>

int main() {
    int c;

    c = getchar();
    while (c != EOF) {
        if (c == '\n' || c == ' ')
            putchar(c);
        else
            putchar(c + 1);

        c = getchar();
    }

    return 0;
}
