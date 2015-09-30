package antlr4

import spock.lang.Specification

import static rx.observables.StringObservable.from
import static rx.observables.StringObservable.byLine

class SyntaxOnlyRealTests extends Specification {
    def s = [] as Set
    def q = [] as Set
    def e = []

    def "error input"() {
        when:
            QP.parse("l", s, q, e)
            //def l = Utils.getLinesCP("q-epbygomw0024.txt")
        then:
            e.size() == 0
        where:
            l << Utils.getLinesCP("q-qa10.txt") // epbygomw0024
    }
}
