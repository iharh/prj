package antlr4

import spock.lang.Specification

class Antlr4Tests extends Specification {

    def "QP basic stuff"() {
        //given:
        when:
            def inputString = "w1 or w2 or w3 and w4 and (w5 or w6 and (w7 and w8)) and w9 or (w10 and w11)"
            def values = QP.parse(inputString)
        then:
            values as Set == ["w1", "w2", "w3", "w4", "w9"] as Set
    }
}
