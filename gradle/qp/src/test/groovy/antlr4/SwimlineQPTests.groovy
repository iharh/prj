package antlr4

import spock.lang.Specification

class SwimlineQPTests extends Specification {
    def s = [] as Set
    def q = [] as Set

    def "swimline qp"() {
        when:
            QP.parse(input, s, q)
        then:
            s == simple as Set
            q == quoted as Set
        where:
            input                                | simple                | quoted
            "Wo*rd1, wprds2?, \"wasdh^&^or*d3\"" | ["Wo*rd1", "wprds2?"] | ["\"wasdh^&^or*d3\""] // basic
            "\"<:)>\""                           | []                    | ["\"<:)>\""]          // smiley and quoted
            "&& || OR AND and or | |"            | []                    | []                    // tricky
            "ATTR:\"abc\\\"def\""                | []                    | []                    // field last quote
            "ATTR:\"+-!(){}[]^\\\"~*?\\ \""      | []                    | []                    // field no escape inside quotes
            "ATTR:\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\"                             | [] | [] // field escaped
            "REF_URL:http\\://mail.aol*, REF_URL:http\\://mail.aol?, REF_URL:\"http://mail.aol\"" | [] | [] // field url
            "business_date: [32 to kjdfhsdjf] doc_date: [32 23] (Cuban AND Crisis ) \" AND ()asjd \"  Location: \"New York\" &&" | [] | ["\" AND ()asjd \""] // range
            "_catRef:[model:\"v1Products Model - Tuning Base Model\" path:\"v1Enterprise|Poweredge Modular Servers\" node:\"v1Mellanox IB m2401g\"], w1" | ["w1"] | [] // catref
            "_lc:(acheter\\ -->\\ fleur_?) _lc:(hello\\ -->\\ word_1) _lc:(good*\\ -->\\ xxx_?)"  | []                             | [] // lc
            "_catRollup:\"abc - def\", _catRollup :abc1, w2"                                      | ["w2"]                         | [] // catrollup
            "w1, w2, w3 and w4 and (w5, w6 and (w7 and w8)) and w9 or (w10, w11)"                 | ["w1", "w2", "w3", "w4", "w9"] | [] // levels2
            "w1 or w2 or w3 and w4 and (w5 or w6 and (w7 and w8)) and w9 or (w10 and w11)"        | ["w1", "w2", "w3", "w4", "w9"] | [] // levels1
    }
}
