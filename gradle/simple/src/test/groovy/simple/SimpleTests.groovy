package simple

import spock.lang.Specification

class SimpleTests extends Specification {
    def "basic stuff"() {
        //given:
        when:
            def s = new Simple()
        then:
            s.getN() == 5
    }
    def "length of crew member names"() {
        expect:
            name.length() == length
        where:
            name     | length
            "Spock"  | 5
            "Kirk"   | 4
            "Scotty" | 6
    }
}
