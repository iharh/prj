#include <lttoolbox/fst_processor.h>
//#include <lttoolbox/lttoolbox_config.h>
#include <lttoolbox/my_stdio.h>
//#include <lttoolbox/lt_locale.h>

#include <cstdlib>
#include <iostream>
//#include <libgen.h>

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

int
main(int argc, char *argv[])
{
    FSTProcessor fstp;
    fstp.setDictionaryCaseMode(true); // -w option

    /* !!!
    LtLocale::tryToSetLocale();
    */

    if (argc < 4)
    {
        std::wcerr << "Error: need at least 3 args, but specified only "  << argc - 1 << std::endl << std::endl;
        exit(EXIT_FAILURE);
    }

    {
        FileHolder in(argv[1], "rb");
        fstp.load(in.get());
    }

    FileHolder input(argv[2], "rb");
    FileHolder output(argv[3], "wb");

    try
    {
        // !!! fstp.initAnalysis();

        if (!fstp.valid())
        {
            exit(EXIT_FAILURE);
        }
        // !!! fstp.analysis(input.get(), output.get());
    }
    catch (std::exception &e)
    {
        std::wcerr << e.what();
        /* !!! if (fstp.getNullFlush())
        {
            fputwc_unlocked(L'\0', output.get);
        }*/
        exit(1);
    }

    std::wcout << "hello lt!" << std::endl;

    return EXIT_SUCCESS;
}
