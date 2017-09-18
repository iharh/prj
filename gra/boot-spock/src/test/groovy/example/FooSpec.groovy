package example

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

import org.springframework.http.HttpStatus

import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FooSpec extends Specification {
    @Autowired
    TestRestTemplate restTemplate

    def '/foo should return world'() {
        when:
        def entity = restTemplate.getForEntity('/foo', Hello)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body.name == 'world'
    }
}
