package antlr4

import spock.lang.Specification

class SintaxOnlyQPTests extends Specification {
    def s = [] as Set
    def q = [] as Set
    def e = []

    def "error input"() {
        when:
            QP.parse(")(", s, q, e)
        then:
            e.size() > 0 //notThrown(Exception)
    }
}
