package antlr4

import spock.lang.Specification
import spock.lang.Ignore
import spock.lang.IgnoreRest

class SwimlineQPTests extends Specification {
    def s = [] as Set
    def w = [] as Set
    def q = [] as Set
    def m = [] as Set
    def e = []

    //@Ignore
    //@IgnoreRest
    def "swimline qp"() {
        when:
            QP.parse(input, s, w, q, m, e)
        then:
            s == simple as Set
            w == wildcard as Set
            q == quoted as Set
            m == mtoken as Set
            e.size() == ecnt
        where:
            input                                | simple | wildcard              | quoted                | mtoken     | ecnt
            "S:d"                                | []     | []                    | []                    | []         | 1
            "S:w"                                | []     | []                    | []                    | []         | 1
            "\"found bug\" ~2,"                  | []     | []                    | ["\"found bug\""]     | []         | 1
            "&"                                  | []     | []                    | []                    | []         | 1
            "FIELD:v"                            | []     | []                    | []                    | []         | 0
            "_mtoken:\":)\""                     | []     | []                    | []                    | ["\":)\""] | 0
            "Wo*rd1, wprds2?, \"wasdh^&^or*d3\"" | []     | ["Wo*rd1", "wprds2?"] | ["\"wasdh^&^or*d3\""] | []         | 0 // basic
            "\"<:)>\""                           | []     | []                    | ["\"<:)>\""]          | []         | 0 // smiley and quoted
            "&& || OR AND and or | |"            | []     | []                    | []                    | []         | 1 // tricky
            "ATTR:\"abc\\\"def\""                | []     | []                    | []                    | []         | 0 // field last quote
            "ATTR:\"+-!(){}[]^\\\"~*?\\ \""      | []     | []                    | []                    | []         | 0 // field no escape inside quotes
            "ATTR:\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\"                                                          | [] | [] | []                   | [] | 0 // field escaped
            "REF_URL:http\\://mail.aol*, REF_URL:http\\://mail.aol?, REF_URL:\"http://mail.aol\""                              | [] | [] | []                   | [] | 0 // field url
            "business_date: [32 to kjdfhsdjf] doc_date: [32 23] (Cuban AND Crisis ) \" AND ()asjd \"  Location: \"New York\" " | [] | [] | ["\" AND ()asjd \""] | [] | 1 // range &&
            "_catRef:[model:\"v1Products Model - Tuning Base Model\" path:\"v1Enterprise|Poweredge Modular Servers\" node:\"v1Mellanox IB m2401g\"], w1" | ["w1"] | [] | [] | [] | 0 // catref
            "_lc:[room, clean]"     | [] | [] | [] | [] | 0 // lc 1
            "_lc:[abc\\ -->\\ def]" | [] | [] | [] | [] | 1 // lc 2
            "_lc:(acheter\\ -->\\ fleur_?) _lc:(hello\\ -->\\ word_1) _lc:(good*\\ -->\\ xxx_?)"  | []                             | [] | [] | [] | 3 // lc 2
            "_catRollup:\"abc - def\", _catRollup :abc1, w2"                                      | ["w2"]                         | [] | [] | [] | 0 // catrollup
            "w1, w2, w3 and w4 and (w5, w6 and (w7 and w8)) and w9 or (w10, w11)"                 | ["w1", "w2", "w3", "w4", "w9"] | [] | [] | [] | 0 // levels2
            "w1 or w2 or w3 and w4 and (w5 or w6 and (w7 and w8)) and w9 or (w10 and w11)"        | ["w1", "w2", "w3", "w4", "w9"] | [] | [] | [] | 0 // levels1
    }
}
