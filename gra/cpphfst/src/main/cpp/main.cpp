#include <iostream>

#include "hfst-optimized-lookup.h"

int
main(void)
{
    std::cout << "start" << std::endl;
    while (true) {
        // int *pI = new int;
        // delete pI;
        //
        FILE *f = ::fopen("d:\\download\\turkish.morph", "rb");

        TransducerHeader header(f);
        TransducerAlphabet alphabet(f, header.symbol_count());

        if (alphabet.get_state_size() == 0)
        {   // if the state size is zero, there are no flag diacritics to handle
            if (header.probe_flag(Weighted))
            {
                // no flags, weights, all analyses
                TransducerW *pT = new TransducerW(f, header, alphabet);
                // std::cout << "TransducerW created" << std::endl;
                delete pT;
            }
        }

        ::fclose(f);
    }
    std::cout << "finish" << std::endl;
    return 0;
}
