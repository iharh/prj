#include <stdio.h>
#include <string.h>

//include "/home/cld/cld2/internal/compact_lang_det_impl.h"
#include "encodings.h"
#include "compact_lang_det_impl.h"

int
main(int argc, char **argv)
{
    const char *buffer = "I like my round table";
    int buffer_length = strlen(buffer);

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

    /*Language lang =*/ CLD2::DetectLanguageSummaryV2(
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
        &is_reliable);

    // Default to English
    //if (lang == CLD2::UNKNOWN_LANGUAGE) {
    //    lang = ENGLISH;
    //}

    printf("Hello cld2\n");
    //return lang;

    return 0;
}
