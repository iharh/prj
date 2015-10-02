package antlr4

import spock.lang.Specification

import static rx.observables.StringObservable.from
import static rx.observables.StringObservable.byLine

class SyntaxOnlyDummyTests extends Specification {
    def s = [] as Set
    def w = [] as Set
    def q = [] as Set
    def m = [] as Set
    def e = []

    def "error input"() {
        when:
            QP.parse(")(", s, w, q, m, e)
            def l = Utils.getLinesCP("q-dummy.txt")
        then:
            e.size() > 0 //notThrown(Exception)
            l.size() == 3;
            l == ["round", "table", "kitchen"]
    }
}
