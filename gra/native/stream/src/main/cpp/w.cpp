#include <cstring>
#include <iostream>
#include <fstream>
#include <locale>

void
tryToSetLocale()
{
    try {
        std::locale::global(std::locale(std::locale::classic(), "", std::locale::ctype));
    }
    catch (...) {
        // Nothing
    }

    if (setlocale(LC_CTYPE, "") != NULL)
    {
        return;
    }

    std::wcerr << L"Warning: unsupported locale, fallback to \"C\"" << std::endl;

    setlocale(LC_ALL, "C");
}

unsigned int
multibyte_read(std::istream &input)
{
  unsigned char up;
  unsigned int result = 0;

  input.read(reinterpret_cast<char *>(&up), sizeof(char));
  if(up < 0x40)
  {
    result = (unsigned int) up;
  }
  else if(up < 0x80)
  {
    up = up & 0x3f;
    unsigned int aux = (unsigned int) up;
    aux = aux << 8;
    unsigned char low;
    input.read(reinterpret_cast<char *>(&low), sizeof(char));
    result = (unsigned int) low;
    result = result | aux;
  }
  else if(up < 0xc0)
  {
    up = up & 0x3f;
    unsigned int aux = (unsigned int) up;
    aux = aux << 8;
    unsigned char middle;
    input.read(reinterpret_cast<char *>(&middle), sizeof(char));
    result = (unsigned int) middle;
    aux = result | aux;
    aux = aux << 8;
    unsigned char low;
    input.read(reinterpret_cast<char *>(&low), sizeof(char));
    result = (unsigned int) low;
    result = result | aux;
  }
  else
  {
    up = up & 0x3f;
    unsigned int aux = (unsigned int) up;
    aux = aux << 8;
    unsigned char middleup;
    input.read(reinterpret_cast<char *>(&middleup), sizeof(char));
    result = (unsigned int) middleup;
    aux = result | aux;
    aux = aux << 8;
    unsigned char middlelow;
    input.read(reinterpret_cast<char *>(&middlelow), sizeof(char));
    result = (unsigned int) middlelow;
    aux = result | aux;
    aux = aux << 8;
    unsigned char low;
    input.read(reinterpret_cast<char *>(&low), sizeof(char));
    result = (unsigned int) low;
    result = result | aux;
  }

  return result;
}

// Global lttoolbox features
constexpr char HEADER_LTTOOLBOX[4]{'L', 'T', 'T', 'B'};
enum LT_FEATURES : uint32_t {
    LTF_UNKNOWN = (1u << 0), // Features >= this are unknown, so throw an error; Inc this if more features are added
    LTF_RESERVED = (1u << 31), // If we ever reach this many feature flags, we need a flag to know how to extend beyond 32 bits
};

void
load(std::istream &input)
{
    if (input.tellg() == 0) {
        char header[4]{};
        input.read(header, 4);
        if (strncmp(header, HEADER_LTTOOLBOX, 4) == 0) {
            auto features = multibyte_read(input);
            if (features >= LTF_UNKNOWN) {
                throw std::runtime_error("FST has features that are unknown to this version of lttoolbox - upgrade!");
            }
        }
        else {
            // Old binary format
            input.seekg(0, input.beg);
        }
    }

    // letters
    int len = multibyte_read(input);
    std::wcout << L"alphabet len: " << len << std::endl;
}

int
main()
{
    const char *fstFileName = "ben/ben.automorf.bin";

    tryToSetLocale();

    std::ifstream ifs(fstFileName, std::ifstream::binary);
    load(ifs);
/*
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
*/
    return 0;
}
