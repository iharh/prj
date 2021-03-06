package com.clarabridge.cld2;

public class Cld2Mapper {
    public static final String UN = "@u";

    private static String [] CLD2_CODES_LO = {
        "en",   // 000, en, English
        "da",   // 001, da, Danish
        "nl",   // 002, nl, Dutch
        "fi",   // 003, fi, Finish
        "fr",   // 004, fr, French
        "de",   // 005, de, German
        "he",   // 006, iw, Hebrew !!!
        "it",   // 007, it, Italian
        "ja",   // 008, ja, Japanese
        "ko",   // 009, ko, Korean
        "no",   // 010, no, Norwegian
        "pl",   // 011, pl, Polish
        "pt",   // 012, pt, Portuguese
        "ru",   // 013, ru, Russian
        "es",   // 014, es, Spanish
        "sv",   // 015, sw, Swedish
        "zh",   // 016, zh, Chinese
        "cs",   // 017, cs, Czech
        "el",   // 018, el, Greek
        "is",   // 019, is, Icelandic
        "lv",   // 020, lv, Latvian
        "lt",   // 021, lt, Lithuanian
        "ro",   // 022, ro, Romanian
        "hu",   // 023, hu, Hungarian
        "et",   // 024, et, Estonian
        UN  ,   // 025, xxx, TGUnknown
        UN  ,   // 026, xxx, Unknown
        "bg",   // 027, bg, Bulgarian
        "hr",   // 028, hr, Croatian
        "sr",   // 029, sr, Serbian
        "ga",   // 030, ir, Irish
        "gl",   // 031, gl, Galician
        "tl",   // 032, tl, Tagalog
        "tr",   // 033, tr, Turkish
        "uk",   // 034, uk, Ukrainian
        "hi",   // 035, hi, Hindi
        "mk",   // 036, mk, Macedonian
        "bn",   // 037, bn, Bengali
        "id",   // 038, id, Indonesian
        "la",   // 039, la, Latin
        "ms",   // 040, ms, Malay
        "ml",   // 041, ml, Malayalam
        "cy",   // 042, cy, Welsh
        "ne",   // 043, ne, Nepali
        "te",   // 044, te, Telugu
        "sq",   // 045, sq, Albanian
        "ta",   // 046, ta, Tamil
        "be",   // 047, be, Belarusian
        "jw",   // 048, jw, Javanese
        "oc",   // 049, oc, Occitan
        "ur",   // 050, ur, Urdu
        "bh",   // 051, bh, Bihari
        "gu",   // 052, gu, Gujarati
        "th",   // 053, th, Thai
        "ar",   // 054, ar, Arabic
        "ca",   // 055, ca, Catalan
        "eo",   // 056, eo, Esperanto
        "eu",   // 057, eu, Basque
        "ia",   // 058, ia, Interlingua
        "kn",   // 059, kn, Kannada
        "pa",   // 060, pa, Panjabi
        "gd",   // 061, gd, StocsGaelic
        "sw",   // 062, sw, Swahili
        "sl",   // 063, sl, Slovenian
        "mr",   // 064, mr, Marathi
        "mt",   // 065, mt, Maltese
        "vi",   // 066, vi, Vietnamese
        "fy",   // 067, fy, Frisian
        "sk",   // 068, sk, Slovak
        "zh",   // 069, zh-Hant, Chinese_T
        "fo",   // 070, fo, Faroese
        "su",   // 071, su, Sundanese
        "uz",   // 072, uz, Uzbek
        "am",   // 073, am, Amharic
        "az",   // 074, az, Azerbaijani
        "ka",   // 075, ka, Georgian
        "ti",   // 076, ti, Tigrinya
        "fa",   // 077, fa, Persian
        "bs",   // 078, bs, Bosnian
        "si",   // 079, si, Sinhalese
        "nn",   // 080, nn, Norwegian_N
        UN,     // 081
        UN,     // 082
        "xh",   // 083, xh, Xhosa
        "zu",   // 084, zu, Zulu
        "gn",   // 085, gn, Guarani
        "st",   // 086, st, Sesotho
        "tk",   // 087, tk, Turkment
        "ky",   // 088, ky, Kyrgyz
        "br",   // 089, br, Breton
        "tw",   // 090, tw, Twi
        "yi",   // 091, yi, Yiddish
        UN,     // 092
        "so",   // 093, so, Somali
        "ug",   // 094, ug, Uighur
        "ku",   // 095, ku, Kurdish
        "mn",   // 096, mn, Mongolian
        "hy",   // 097, hy, Armenian
        "lo",   // 098, lo, Loathian
        "sd",   // 099, sd, Sindhi
        "rm",   // 100, rm, RhaetoRomance
        "af",   // 101, af, Afrikaans
        "lb",   // 102, lb, Luxembourgish
        "my",   // 103, my, Burmese
        "km",   // 104, km, Khmer
        "bo",   // 105, bo, Tibetan
        "dv",   // 106, dv, Dhivehi
        UN  ,   // 107, chr, Cherokee !!!
        UN  ,   // 108, syr, Syriac !!!
        "li",   // 109, lif, Limbu !!! Limburgish
        "or",   // 110, or, Oriya
        "as",   // 111, as, Assamese
        "co",   // 112, co, Corsican
        "ie",   // 113, ie, Interlingue
        "kk",   // 114, kk, Kazakh
        "ln",   // 115, ln, Lingala
        UN,     // 116
        "ps",   // 117, ps, Pashto
        "qu",   // 118, qu, Quechua
        "sn",   // 119, sn, Shona
        "tg",   // 120, tg, Tajik
        "tt",   // 121, tt, Tatar
        "to",   // 122, to, Tonga
        "yo",   // 123, yo, Yoruba
        UN,     // 124
        UN,     // 125
        UN,     // 126
        UN,     // 127
        "mi",   // 128, mi, Maori
        "wo",   // 129, wo, Wolof
        "ab",   // 130, ab, Abkhazian
        "aa",   // 131, aa, Afar
        "ay",   // 132, ay, Aymara
        "ba",   // 133, ba, Bashkir
        "bi",   // 134, bi, Bislama
        "dz",   // 135, dz, Dzongkha
        "fj",   // 136, fj, Fijian
        "kl",   // 137, kl, Greenlandic !!! Kalaallisut
        "ha",   // 138, ha, Hausa
        "ht",   // 139, ht, HaitianCreole
        "ik",   // 140, ik, Inupiak
        "iu",   // 141, iu, Inuktitut
        "ks",   // 142, ks, Kashmiri
        "rw",   // 143, rw, Kinyarwanda
        "mg",   // 144, mg, Malagasy
        "na",   // 145, na, Nauru
        "om",   // 146, om, Oromo
        "rn",   // 147, rn, Rundi
        "sm",   // 148, sm, Samoan
        "sg",   // 149, sg, Sango
        "sa",   // 150, sa, Sanskrit
        "ss",   // 151, ss, Siswant !!! Swati
        "ts",   // 152, ts, Tsonga
        "tn",   // 153, tn, Tswana
        "vo",   // 154, vo, Volapuk
        "za",   // 155, za, Zhuang
        UN  ,   // 156, kha, Khasi !!!
        UN  ,   // 157, sco, Scots !!!
        "lg",   // 158, lg, Ganda
        "gv",   // 159, gv, Manx
        UN  ,   // 160, sr-ME, Montenegrin !!!
        "ak",   // 161, ak, Akan
        "ig",   // 162, ig, Igbo
        UN  ,   // 163, mfe, MauritianCreole !!!
        UN  ,   // 164, haw, Hawaiian !!!
        UN  ,   // 165, ceb, Cebuano !!!
        "ee",   // 166, ee, Ewe
        UN  ,   // 167, gaa, Ga !!!
        UN  ,   // 168, blu, Hmong !!!
        UN  ,   // 169, kri, Krio !!!
        UN  ,   // 170, loz, Lozi
        "lu",   // 171, lua, LubaLulua !!!
        "lu",   // 172, luo, LuoKenyaAndTanzania !!!
        UN  ,   // 173, new, Hewari !!!
        "ny",   // 174, ny, Nyanja !!! Chichewa
        "os",   // 175, os, Ossetian
        UN  ,   // 176, pam, Pampanga !!!
        UN  ,   // 177, nso, Pedi !!!
        UN  ,   // 178, raj, Rajasthani !!!
        UN  ,   // 179, crs, Seselwa !!!
        UN  ,   // 180, tum, Tumbuka !!!
        "ve",   // 181, ve, Venda
        UN  ,   // 182, war, WarayPhilippines !!!
    };

    private static String [] CLD2_CODES_HI = {
        "nr",   // 506, nr, Ndebele
        "zzb",  // 507, zzb, XBorkBorkBork
        "zzp",  // 508, zzp, XPigLatin
        "zzh",  // 509, zzh, XHacker
        "tlh",  // 510, tlh, XKlingon
        "zze",  // 511, zze, XElmerFudd
/*
X_Common                     = 512,  // xx-Zyyy
X_Latin                      = 513,  // xx-Latn
X_Greek                      = 514,  // xx-Grek
X_Cyrillic                   = 515,  // xx-Cyrl
X_Armenian                   = 516,  // xx-Armn
X_Hebrew                     = 517,  // xx-Hebr
X_Arabic                     = 518,  // xx-Arab
X_Syriac                     = 519,  // xx-Syrc
X_Thaana                     = 520,  // xx-Thaa
X_Devanagari                 = 521,  // xx-Deva
X_Bengali                    = 522,  // xx-Beng
X_Gurmukhi                   = 523,  // xx-Guru
X_Gujarati                   = 524,  // xx-Gujr
X_Oriya                      = 525,  // xx-Orya
X_Tamil                      = 526,  // xx-Taml
X_Telugu                     = 527,  // xx-Telu
X_Kannada                    = 528,  // xx-Knda
X_Malayalam                  = 529,  // xx-Mlym
X_Sinhala                    = 530,  // xx-Sinh
X_Thai                       = 531,  // xx-Thai
X_Lao                        = 532,  // xx-Laoo
X_Tibetan                    = 533,  // xx-Tibt
X_Myanmar                    = 534,  // xx-Mymr
X_Georgian                   = 535,  // xx-Geor
X_Hangul                     = 536,  // xx-Hang
X_Ethiopic                   = 537,  // xx-Ethi
X_Cherokee                   = 538,  // xx-Cher
X_Canadian_Aboriginal        = 539,  // xx-Cans
X_Ogham                      = 540,  // xx-Ogam
X_Runic                      = 541,  // xx-Runr
X_Khmer                      = 542,  // xx-Khmr
X_Mongolian                  = 543,  // xx-Mong
X_Hiragana                   = 544,  // xx-Hira
X_Katakana                   = 545,  // xx-Kana
X_Bopomofo                   = 546,  // xx-Bopo
X_Han                        = 547,  // xx-Hani
X_Yi                         = 548,  // xx-Yiii
X_Old_Italic                 = 549,  // xx-Ital
X_Gothic                     = 550,  // xx-Goth
X_Deseret                    = 551,  // xx-Dsrt
X_Inherited                  = 552,  // xx-Qaai
X_Tagalog                    = 553,  // xx-Tglg
X_Hanunoo                    = 554,  // xx-Hano
X_Buhid                      = 555,  // xx-Buhd
X_Tagbanwa                   = 556,  // xx-Tagb
X_Limbu                      = 557,  // xx-Limb
X_Tai_Le                     = 558,  // xx-Tale
X_Linear_B                   = 559,  // xx-Linb
X_Ugaritic                   = 560,  // xx-Ugar
X_Shavian                    = 561,  // xx-Shaw
X_Osmanya                    = 562,  // xx-Osma
X_Cypriot                    = 563,  // xx-Cprt
X_Braille                    = 564,  // xx-Brai
X_Buginese                   = 565,  // xx-Bugi
X_Coptic                     = 566,  // xx-Copt
X_New_Tai_Lue                = 567,  // xx-Talu
X_Glagolitic                 = 568,  // xx-Glag
X_Tifinagh                   = 569,  // xx-Tfng
X_Syloti_Nagri               = 570,  // xx-Sylo
X_Old_Persian                = 571,  // xx-Xpeo
X_Kharoshthi                 = 572,  // xx-Khar
X_Balinese                   = 573,  // xx-Bali
X_Cuneiform                  = 574,  // xx-Xsux
X_Phoenician                 = 575,  // xx-Phnx
X_Phags_Pa                   = 576,  // xx-Phag
X_Nko                        = 577,  // xx-Nkoo
X_Sundanese                  = 578,  // xx-Sund
X_Lepcha                     = 579,  // xx-Lepc
X_Ol_Chiki                   = 580,  // xx-Olck
X_Vai                        = 581,  // xx-Vaii
X_Saurashtra                 = 582,  // xx-Saur
X_Kayah_Li                   = 583,  // xx-Kali
X_Rejang                     = 584,  // xx-Rjng
X_Lycian                     = 585,  // xx-Lyci
X_Carian                     = 586,  // xx-Cari
X_Lydian                     = 587,  // xx-Lydi
X_Cham                       = 588,  // xx-Cham
X_Tai_Tham                   = 589,  // xx-Lana
X_Tai_Viet                   = 590,  // xx-Tavt
X_Avestan                    = 591,  // xx-Avst
X_Egyptian_Hieroglyphs       = 592,  // xx-Egyp
X_Samaritan                  = 593,  // xx-Samr
X_Lisu                       = 594,  // xx-Lisu
X_Bamum                      = 595,  // xx-Bamu
X_Javanese                   = 596,  // xx-Java
X_Meetei_Mayek               = 597,  // xx-Mtei
X_Imperial_Aramaic           = 598,  // xx-Armi
X_Old_South_Arabian          = 599,  // xx-Sarb
X_Inscriptional_Parthian     = 600,  // xx-Prti
X_Inscriptional_Pahlavi      = 601,  // xx-Phli
X_Old_Turkic                 = 602,  // xx-Orkh
X_Kaithi                     = 603,  // xx-Kthi
X_Batak                      = 604,  // xx-Batk
X_Brahmi                     = 605,  // xx-Brah
X_Mandaic                    = 606,  // xx-Mand
X_Chakma                     = 607,  // xx-Cakm
X_Meroitic_Cursive           = 608,  // xx-Merc
X_Meroitic_Hieroglyphs       = 609,  // xx-Mero
X_Miao                       = 610,  // xx-Plrd
X_Sharada                    = 611,  // xx-Shrd
X_Sora_Sompeng               = 612,  // xx-Sora
X_Takri                      = 613,  // xx-Takr
*/
    };

    public static String mapToCode(int idx) {
        return idx >= 0 && idx < CLD2_CODES_LO.length ? CLD2_CODES_LO[idx] : UN;
    }
}
