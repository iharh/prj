#include <memory>

#include <stdio.h>
#include <string.h>

#include "encodings.h"
#include "lang_script.h"
#include "compact_lang_det_impl.h"

#include "csv.h"

int
main(int argc, char **argv)
{
    io::CSVReader<2> in("in.csv");
    //in.read_header(io::ignore_extra_column, "vendor", "size", "speed");

    std::string expectedLang("");
    std::string text;

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

    printf("exp-code, exp-id, det-code, det-id, text\n");
    while (in.read_row(expectedLang, text))
    {
        CLD2::Language expectedLangId = CLD2::GetLanguageFromName(expectedLang.c_str());
        CLD2::Language detectedLangId = CLD2::DetectLanguageSummaryV2(
            //buffer,
            //strlen(buffer),
            text.c_str(),
            text.size(),
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

        printf("%s, %d, %s, %d, %s\n"
            , expectedLang.c_str()
            , expectedLangId
            , CLD2::LanguageCode(detectedLangId)
            , detectedLangId
            , text.c_str());
    }
    return 0;
}
