package antlr4

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

import static rx.observables.StringObservable.from
import static rx.observables.StringObservable.byLine

class SyntaxOnlyRealTests extends Specification {
    def s = [] as Set
    def w = [] as Set
    def q = [] as Set
    def m = [] as Set
    def e = []

    @Shared def realSwimlines = []
	
    def setupSpec() {
	//realSwimlines = new File(new ClassPathResource('swimlines/swimlines.txt').getURI()) as String[]
	realSwimlines = Utils.getLinesCP("q-qa10.txt")
    }

    @Unroll
    def "qa10 idx: #n rule: #l"() {
        when:
            QP.parse(l, s, w, q, m, e)
        then:
            e.size() == 0
        where:
            l << realSwimlines
            n << (1 .. realSwimlines.size())
    }
}
