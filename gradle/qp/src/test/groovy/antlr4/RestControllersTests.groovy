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
            QP.parse("w1, w2, w3 and w4 and (w5, w6 and (w7 and w8)) and w9 or (w10, w11)")
        then:
            s == ["w1", "w2", "w3", "w4", "w9"] as Set
            q == [] as Set
    }
/*
    def "catref"() {
        when:
            def r = QP.parse("_catRef:[model:\"v1Products Model - Tuning Base Model\" path:\"v1Enterprise|Poweredge Modular Servers\" node:\"v1Mellanox IB m2401g\"], w1")
        then:
            r as Set == ["w1"] as Set
    }
    def "catrollup"() {
        when:
            def r = QP.parse("_catRollup:\"abc - def\", _catRollup :abc1, w2")
        then:
            r as Set == ["w2"] as Set
    }
    def "smiley and quoted"() {
        when:
            def r = QP.parse("\"<:)>\"")
        then:
            r as Set == ["\"<:)>\""] as Set
    }
    def "lc"() {
        when:
            def r = QP.parse("_lc:(acheter\\ -->\\ fleur_?)")
        then:
            r as Set == [] as Set
    }
    def "fields"() {
        when:
            def r = QP.parse(" business_date: [3246883 to kjdfhsdjf]  (Cuban AND Crisis ) \" AND ()asjd \"  Location: \"New York\"  CUSTOMER: Apple   &&")
        then:
            r as Set == ["\" AND ()asjd \""] as Set
    }
*/
}
