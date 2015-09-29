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
    def "lc"() {
        when:
            QP.parse("_lc:(acheter\\ -->\\ fleur_?) _lc:(hello\\ -->\\ word_1) _lc:(good*\\ -->\\ xxx_?)", s, q)
        then:
            s == [] as Set
            q == [] as Set
    }
    def "field range"() {
        when:
            QP.parse(" business_date: [3246883 to kjdfhsdjf] doc_date: [3246883 23432434] (Cuban AND Crisis ) \" AND ()asjd \"  Location: \"New York\"  CUSTOMER: Apple   &&", s, q)
        then:
            s == [] as Set
            q == ["\" AND ()asjd \""] as Set
    }
    def "field url"() {
        when:
            QP.parse("REF_URL:http\\://mail.aol*, REF_URL:http\\://mail.aol?, REF_URL:\"http://mail.aol\"", s, q)
        then:
            s == [] as Set
            q == [] as Set
    }
    def "field escaped"() {
        when:
            QP.parse("ATTR:\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\", s, q)
        then:
            s == [] as Set
            q == [] as Set
    }
    def "field no escape inside quotes"() {
        when:
            QP.parse("ATTR:\"+-!(){}[]^\\\"~*?\\ \"", s, q)
        then:
            s == [] as Set
            q == [] as Set
    }
    def "field last quote"() {
        when:
            QP.parse("ATTR:\"abc\\\"def\"", s, q)
        then:
            s == [] as Set
            q == [] as Set
    }
    def "tricky1"() {
        when:
            QP.parse("&& || OR AND and or | |", s, q)
        then:
            s == [] as Set
            q == [] as Set
    }
}
