//#include <iostream>

#include "hfst-optimized-lookup.h"

int
main(void)
{
    //std::cout << "start" << std::endl;

    /*while (true)*/ {
        //FILE *f = ::fopen("d:\\download\\turkish.morph", "rb");
        FILE *f = ::fopen("./bin/turkish.morph", "rb");
        TransducerHeader header(f);
        TransducerAlphabet alphabet(f, header.symbol_count());

        TransducerW *pT = new TransducerW(f, header, alphabet);
        delete pT;

        ::fclose(f);
    }
    //std::cout << "finish" << std::endl;
    return 0;
}
