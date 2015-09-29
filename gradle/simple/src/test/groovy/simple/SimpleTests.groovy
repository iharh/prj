package simple

import spock.lang.Specification
import spock.lang.Unroll

class SimpleTests extends Specification {
    def "basic stuff"() {
        //given:
        when:
            def s = new Simple()
        then:
            s.getN() == 5
    }
    @Unroll
    def "crew member name: '#name' length: #length"() {
        expect:
            name.length() == length
        where:
            name     | length
            "Spock"  | 5
            "Kirk"   | 4
            "Scotty" | 6
    }
}
