package rest

import spock.lang.Specification
import spock.lang.Unroll

class RESTTests extends Specification {
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
