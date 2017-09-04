#include "hfst-optimized-lookup.h"

int
main(void)
{
    FILE *f = ::fopen("./bin/turkish.morph", "rb");
    TransducerHeader header(f);
    std::shared_ptr<TransducerAlphabet> pAlphabet(new TransducerAlphabet(f, header.symbol_count()));

    TransducerW *pT = new TransducerW(f, header, pAlphabet);
    delete pT;

    ::fclose(f);
    return 0;
}
