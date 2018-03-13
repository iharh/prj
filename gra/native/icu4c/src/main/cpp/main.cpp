#include <unicode/unistr.h>

#include <iostream>
#include <locale>

namespace std
{
    inline std::wostream &operator <<(std::wostream &stream, const UnicodeString &s)
    {
        for (int i = 0; i < s.length(); ++i)
            stream.put(s.charAt(i));
        return stream;
    }
}

int
main(void)
{
    //std::locale::global (std::locale ("en_US.UTF-8"));
    setlocale(LC_CTYPE, "en_US.UTF-8");

    UnicodeString orig(UNICODE_STRING_SIMPLE("abc"));

    std::wcout << L"Hello привет world lội " << orig << L" !" << std::endl;
    return 0;
}
