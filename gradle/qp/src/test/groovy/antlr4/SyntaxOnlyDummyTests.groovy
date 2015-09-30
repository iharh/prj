package antlr4

import spock.lang.Specification

import static rx.observables.StringObservable.from
import static rx.observables.StringObservable.byLine

class SyntaxOnlyDummyTests extends Specification {
    def s = [] as Set
    def q = [] as Set
    def e = []

    def "error input"() {
        when:
            QP.parse(")(", s, q, e)
            def l = Utils.getLinesCP("queries.txt")
        then:
            e.size() > 0 //notThrown(Exception)
            l.size() == 3;
            l == ["round", "table", "kitchen"]
    }
}
