#include <stdio.h>
#include <cstring>

#include <malloc.h>

#include "encodings.h"
#include "lang_script.h"
#include "compact_lang_det_impl.h"

#if defined(_MSC_VER)
    #define OVERRIDE override
    #define SEALED sealed
    #define ABSTRACT abstract

    #define CLD_EXPORT extern "C" __declspec(dllexport)

    // We'd like to use override, sealed and abstract which ARE standard now
    #pragma warning(disable:4481)

#elif defined(__GNUC__)
    #define OVERRIDE
    #define SEALED
    #define ABSTRACT

    #define CLD_EXPORT extern "C" __attribute__ ((visibility("default")))

#else
    #error Unsupported compiler
#endif

// generated_language.h - list of detected languages
CLD_EXPORT
int detectLangClb(const char *buffer) {
    CLD2::Language detectedLangId = CLD2::UNKNOWN_LANGUAGE;

    if (buffer != NULL) {
        try {
            int buffer_length = std::strlen(buffer);
            //detectedLangId = static_cast<CLD2::Language>(buffer_length);

            bool is_plain_text = true;

            const char *tld_hint = "";
            int encoding_hint = CLD2::UNKNOWN_ENCODING;
            CLD2::Language language_hint = CLD2::UNKNOWN_LANGUAGE;
            CLD2::CLDHints cldhints = {NULL, tld_hint, encoding_hint, language_hint};

            bool allow_extended_lang = false;

            CLD2::Language language3[3];
            int percent3[3];
            double normalized_score3[3];
            int text_bytes;
            int flags = 0;
            CLD2::Language plus_one = CLD2::UNKNOWN_LANGUAGE;

            bool is_reliable = false;

            detectedLangId = CLD2::DetectLanguageSummaryV2(
                buffer,
                buffer_length,
                is_plain_text,
                &cldhints,
                allow_extended_lang,
                flags,
                plus_one,
                language3,
                percent3,
                normalized_score3,
                NULL,
                &text_bytes,
                &is_reliable
            );
        } catch (...) {
        }
    }
    return static_cast<int>(detectedLangId);
}

CLD_EXPORT
int getMemoryUsage()
{
    int result = 0;
    try
    {
#if defined(_WIN32) || defined(__WIN32__) || defined(WIN32) || defined(_WINDOWS)
        _HEAPINFO hInfo;
        int status;
        hInfo._pentry = NULL;
        while ((status = _heapwalk(&hInfo)) == _HEAPOK)
        {
            if (_USEDENTRY == hInfo._useflag)
                result += hInfo._size;
        }
        /*switch (status)
        {
        case _HEAPEMPTY:
        case _HEAPEND:
            //return result;
        default:
            //JNIUtils::ThrowJavaException(pEnv, "java/lang/RuntimeException", "Heap corrupted");
        }*/
#elif defined __unix__
        struct mallinfo mem_info = ::mallinfo();
        result = (int)mem_info.arena;
#else
    #error "Unsupported platform"
#endif
    }
    catch (...)
    {
        //JNIUtils::ThrowJavaException(pEnv, "java/lang/RuntimeException", "Unknown reason");
    }
    return result; // -1
}
