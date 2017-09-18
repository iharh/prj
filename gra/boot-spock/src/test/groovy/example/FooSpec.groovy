package example

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

import org.springframework.http.HttpStatus

import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

@SpringBootTest(webEnvironment = DEFINED_PORT, properties = ['server.port=18080', 'graphite.enabled=false'])
class FooSpec extends Specification {
    @Autowired
    TestRestTemplate restTemplate

    @Shared
    int port = 18080;

    def '/foo should return world'() {
        when:
        def entity = restTemplate.getForEntity('/foo', Hello)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body.name == 'world'
    }
}
