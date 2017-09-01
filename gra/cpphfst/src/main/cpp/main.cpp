#include "hfst-optimized-lookup.h"

int
main(void)
{
    FILE *f = ::fopen("./bin/turkish.morph", "rb");
    TransducerHeader header(f);
    TransducerAlphabet alphabet(f, header.symbol_count());

    TransducerW *pT = new TransducerW(f, header, alphabet);
    delete pT;

    ::fclose(f);

    return 0;
}
