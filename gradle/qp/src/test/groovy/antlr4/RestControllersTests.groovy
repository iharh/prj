package antlr4

import spock.lang.Specification

class Antlr4Tests extends Specification {
    def s = [] as Set
    def q = [] as Set

    def "basic"() {
        when:
            QP.parse("Wo*rd1, wprds2?, \"wasdh^&^or*d3\"", s, q)
        then:
            s == ["Wo*rd1", "wprds2?"] as Set
            q == ["\"wasdh^&^or*d3\""] as Set
    }
    def "levels1"() {
        when:
            QP.parse("w1 or w2 or w3 and w4 and (w5 or w6 and (w7 and w8)) and w9 or (w10 and w11)", s, q)
        then:
            s == ["w1", "w2", "w3", "w4", "w9"] as Set
            q == [] as Set
    }
    def "levels2"() {
        when:
            QP.parse("w1, w2, w3 and w4 and (w5, w6 and (w7 and w8)) and w9 or (w10, w11)", s, q)
        then:
            s == ["w1", "w2", "w3", "w4", "w9"] as Set
            q == [] as Set
    }
    def "catref"() {
        when:
            QP.parse("_catRef:[model:\"v1Products Model - Tuning Base Model\" path:\"v1Enterprise|Poweredge Modular Servers\" node:\"v1Mellanox IB m2401g\"], w1", s, q)
        then:
            s == ["w1"] as Set
    }
    def "catrollup"() {
        when:
            QP.parse("_catRollup:\"abc - def\", _catRollup :abc1, w2", s, q)
        then:
            s == ["w2"] as Set
    }
    def "smiley and quoted"() {
        when:
            QP.parse("\"<:)>\"", s, q)
        then:
            s == [] as Set
            q == ["\"<:)>\""] as Set
    }
    def "table"() {
        when:
            QP.parse(input, s, q)
        then:
            s == simple as Set
            q == quoted as Set
        where:
            input                           | simple | quoted
            "&& || OR AND and or | |"       | [] | [] // tricky
            "ATTR:\"abc\\\"def\""           | [] | [] // field last quote
            "ATTR:\"+-!(){}[]^\\\"~*?\\ \"" | [] | [] // field no escape inside quotes
            "ATTR:\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\"                             | [] | [] // field escaped
            "REF_URL:http\\://mail.aol*, REF_URL:http\\://mail.aol?, REF_URL:\"http://mail.aol\"" | [] | [] // field url
            "business_date: [32 to kjdfhsdjf] doc_date: [32 23] (Cuban AND Crisis ) \" AND ()asjd \"  Location: \"New York\" &&" | [] | ["\" AND ()asjd \""] // range
            "_lc:(acheter\\ -->\\ fleur_?) _lc:(hello\\ -->\\ word_1) _lc:(good*\\ -->\\ xxx_?)"  | [] | [] // lc
    }
}