#include <unicode/unistr.h>
#include <unicode/normlzr.h>

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

void
compDecomp(const UnicodeString &orig)
{
    UnicodeString composed;
    UErrorCode status = U_ZERO_ERROR;
    Normalizer::compose(orig, TRUE, 0, composed, status);
    if (U_FAILURE(status))
    {
        std::wcout << L"Normalizer::compose failure detected" << std::endl;
    }
    status = U_ZERO_ERROR;
    UnicodeString decomposed;
    Normalizer::decompose(orig, TRUE, 0, decomposed, status);
    if (U_FAILURE(status))
    {
        std::wcout << L"Normalizer::decompose failure detected" << std::endl;
    }

    std::wcout << orig << L" " << composed << L" " << decomposed << std::endl;
}

int
main(void)
{
    //std::locale::global (std::locale ("en_US.UTF-8"));
    setlocale(LC_CTYPE, "en_US.UTF-8");

    //compDecomp(UNICODE_STRING_SIMPLE("loi"));
    //compDecomp(UNICODE_STRING_SIMPLE("lội"));
    compDecomp(UNICODE_STRING_SIMPLE("ộ"));
    return 0;
}
