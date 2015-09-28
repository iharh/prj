package antlr4

import spock.lang.Specification

class Antlr4Tests extends Specification {

    def "QP basic stuff"() {
        //given:
        when:
            def qp = new QP()
        then:
            qp.getN() == 5
    }
}
