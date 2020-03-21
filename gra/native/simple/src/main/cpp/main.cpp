#include <unicode/unistr.h>
#include <unicode/ustring.h>
#include <unicode/uchar.h>

#include <xercesc/util/PlatformUtils.hpp>
#include <xercesc/util/XMLString.hpp>
#include <xercesc/dom/DOM.hpp>
#include <xercesc/util/OutOfMemoryException.hpp>

#include <cstdlib> // getenv
#include <iostream>

namespace std
{
    inline std::wostream &operator <<(std::wostream &stream, const icu::UnicodeString &s)
    {
        for (int i = 0; i < s.length(); ++i)
            stream.put(s.charAt(i));
        return stream;
    }

    inline std::wistream &operator >>(std::wistream &stream, icu::UnicodeString &s)
    {
        s.truncate(0);
        std::wistream::int_type c = stream.rdbuf()->sbumpc();
        while (std::wistream::traits_type::eof() != c)
        {
            s.append(std::wistream::traits_type::to_char_type(c));
            c = stream.rdbuf()->sbumpc();
        }
        stream.setstate(ios_base::eofbit);
        return stream;
    }
}

int
main()
{
    // Initialize the XML4C2 system.
    try
    {
        xercesc::XMLPlatformUtils::Initialize();
    }

    catch (const xercesc::XMLException &toCatch)
    {
        char *pMsg = xercesc::XMLString::transcode(toCatch.getMessage());
        std::cerr << "Error during Xerces-c Initialization." << std::endl
             << "  Exception message:" << pMsg;
        xercesc::XMLString::release(&pMsg);
        return 1;
    }

    
    icu::UnicodeString prefix = UNICODE_STRING_SIMPLE("icu4c str");

    const char *envVar = std::getenv("ENV_VAR");
    if (envVar != NULL)
    {
        std::wcout << prefix << UNICODE_STRING_SIMPLE(" with ENV_VAR") << std::endl;
    }
    else
    {
        std::wcout << prefix << UNICODE_STRING_SIMPLE(" without ENV_VAR") << std::endl;
    }

    UChar c = 0;
    if (u_isWhitespace(c)) {
        std::wcout << UNICODE_STRING_SIMPLE("u_isWhitespace") << std::endl;
    }
    else {
        std::wcout << UNICODE_STRING_SIMPLE("!u_isWhitespace") << std::endl;
    }
    
    // icu::UnicodeString s1;
    // std::wcin >> s1;

    xercesc::XMLPlatformUtils::Terminate();
    return 0;
}
