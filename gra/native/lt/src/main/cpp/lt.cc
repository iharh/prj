#include <lttoolbox/fst_processor.h>
#include <lttoolbox/my_stdio.h>
#include <lttoolbox/lt_locale.h>

#include <cstdlib>
#include <iostream>
#include <fstream>

class FileHolder {
private:
    FILE *pF;
public:
    FileHolder(const char *file_name, const char *mode)
    {
        pF = fopen(file_name, mode);
        if (pF == NULL || ferror(pF))
        {
            std::wcerr << "Error: Cannot not open file '" << file_name << "'." << std::endl << std::endl;
            exit(EXIT_FAILURE);
        }
    }

    ~FileHolder()
    {
        fclose(pF);
    }

    FILE *
    get()
    {
        return pF;
    }
};

void
printArgs(int argc, char *argv[])
{
    std::wcout << "args:" << std::endl;
    for (int i = 0; i < argc; ++i) {
        std::wcout << argv[i] << std::endl;
    }
}

// 
// using "argc", "argv" var-name is a kind-of magic - switchess off asan

int
main(int argc, char *argv[])
{
/*
    const char *fstFileName = "ben/ben.automorf.bin";
    const char *inFileName  = "data/in.txt";
    const char *outFileName = "data/out.txt";
*/
    const char *fstFileName = argv[1];
    const char *inFileName  = argv[2];
    const char *outFileName = argv[3];

    FSTProcessor fstp;
    fstp.setDictionaryCaseMode(true); // -w option

    LtLocale::tryToSetLocale();

    {
        //FileHolder hFst(fstFileName, "rb");
        std::ifstream ifs(fstFileName, std::ifstream::binary);
        fstp.load(ifs); // hFst.get()
    }

    FileHolder hIn(inFileName, "rb");
    FileHolder hOut(outFileName, "wb");

    try
    {
        fstp.initAnalysis();
        if (!fstp.valid())
        {
            exit(EXIT_FAILURE);
        }

        fstp.analysis(hIn.get(), hOut.get());

        int *pA = new int; *pA = 7;

        std::wcout << "Done." << *pA << std::endl;
    }
    catch (std::exception &e)
    {
        std::wcerr << e.what();
        if (fstp.getNullFlush())
        {
            fputwc_unlocked(L'\0', hOut.get());
        }
        exit(1);
    }

    { int *pB = new int; *pB = 3; }

    return EXIT_SUCCESS;
}
