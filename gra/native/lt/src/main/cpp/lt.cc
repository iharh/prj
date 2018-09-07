#include <lttoolbox/fst_processor.h>
#include <lttoolbox/my_stdio.h>
#include <lttoolbox/lt_locale.h>

#include <cstdlib>
#include <iostream>
#include <fstream>

void
printArgs(int argc, char *argv[])
{
    std::wcout << "args:" << std::endl;
    for (int i = 0; i < argc; ++i) {
        std::wcout << argv[i] << std::endl;
    }
}

int
main(int argc, char *argv[])
{
/*
    const char *fstFileName = "ben/ben.automorf.bin";
    const char *inFileName  = "data/in.txt";
*/
    const char *fstFileName = argv[1];
    const char *inFileName  = argv[2];

    FSTProcessor fstp;
    fstp.setDictionaryCaseMode(true); // -w option

    //std::locale loc(std::locale(std::locale::classic(), "", std::locale::ctype));
    std::locale loc("en_US.UTF8");
    LtLocale::tryToSetLocale();

    {
        std::ifstream ifs(fstFileName, std::ifstream::binary);
        fstp.load(ifs); // hFst.get()
    }

    std::wifstream infs(inFileName, std::wifstream::binary);
    infs.imbue(loc);

    try
    {
        fstp.initAnalysis();
        if (!fstp.valid())
        {
            exit(EXIT_FAILURE);
        }

        fstp.analysis(infs);

        std::wcout << "Done." << std::endl;
    }
    catch (std::exception &e)
    {
        std::wcerr << e.what();
        exit(1);
    }

    return EXIT_SUCCESS;
}
