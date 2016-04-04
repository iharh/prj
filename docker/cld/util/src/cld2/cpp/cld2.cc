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
    if (argc < 3)
    {
        printf("Usage: %s <lang-code> <in.csv>\n", argv[0]);
        return 1;
    }
    const char *expectedLang = argv[1];

    // template<unsigned column_count,
    //   class trim_policy = trim_chars<' ', '\t'>,
    //   class quote_policy = no_quote_escape<','>,
    //   class overflow_policy = throw_on_overflow,
    //   class comment_policy = no_comment
    // >
    //io::CSVReader<2, io::trim_chars<' ', '\t'>, io::double_quote_escape<',','"'> > in(argv[1]);
    //in.read_header(io::ignore_extra_column, "CODE", "TEXT");
    io::CSVReader<1, io::trim_chars<' ', '\t'>, io::double_quote_escape<',','"'> > in(argv[2]);
    in.read_header(io::ignore_extra_column, "TEXT");

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

    printf("#line, expected, detected, text\n");
    CLD2::Language expectedLangId = CLD2::GetLanguageFromName(expectedLang);
    size_t rows = 0;
    size_t rowsMismatch = 0;
    std::string text;
    for (rows = 0; in.read_row(text); ++rows)
    {
        // printf("%s, %s\n", expectedLang.c_str(), text.c_str());
        //CLD2::Language expectedLangId = CLD2::GetLanguageFromName(expectedLang);

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

        if (expectedLangId != detectedLangId)
        {
            ++rowsMismatch;
            printf("%d, %s, %s, %s\n"
                , (rows + 2)
                , expectedLang //.c_str()
                //, expectedLangId
                , CLD2::LanguageCode(detectedLangId)
                //, detectedLangId
                , text.c_str());
        }
    }
    printf("\n");
    printf("rows: %d, rowsMismatch: %d\n", rows, rowsMismatch);
    return 0;
}
