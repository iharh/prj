package antlr4

import spock.lang.Specification
import spock.lang.Unroll

import static rx.observables.StringObservable.from
import static rx.observables.StringObservable.byLine

class SyntaxOnlyRealTests extends Specification {
    def s = [] as Set
    def w = [] as Set
    def q = [] as Set
    def m = [] as Set
    def e = []

    @Unroll
    def "qa10 idx: #n rule: #l"() {
        when:
            QP.parse(l, s, w, q, m, e)
        then:
            e.size() == 0
        where:
            l << Utils.getLinesCP("q-qa10.txt")
            n << (1 .. 5000)
    }
}
