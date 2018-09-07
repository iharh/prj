#include <iostream>
#include <fstream>
#include <locale>

int
main()
{
    std::locale loc(std::locale(std::locale::classic(), "", std::locale::ctype));
    try {
        std::locale::global(loc);
    }
    catch (...) {
        // Nothing
    }

    const char *fileName = "data/in.txt";
    std::wifstream input(fileName, std::wifstream::binary);
    input.imbue(loc);

    if (input)
    {
        //input.seekg(0, input.end);
        //std::wcout << "in size: " << input.tellg() << std::endl;
        //input.seekg(0, input.beg);

        wchar_t c1;
        input >> c1;

        std::wcout << "c: " << c1 << std::endl;
        if (input)
        {
            std::wcout << "ok" << std::endl;
        }
    }
    else
    {
        std::wcout << "in is invalid" << std::endl;
    }


    //while (!input.eof())
    //while (input.get(&c, sizeof(wchar_t)))
    //{
    //    std::wcout << L"readen: " << c << std::endl;
    //}

    // std::wcout << "Done" << std::endl;
}
