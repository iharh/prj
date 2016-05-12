package rest

import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import groovyx.net.http.EncoderRegistry

//import net.sf.json.JSON
//import net.sf.json.JSONArray

import spock.lang.Specification
//import spock.lang.Unroll

class RESTTests extends Specification {
    //@Unroll
    //def "crew member name: '#name' length: #length"() {

    def "test rest"() {
        when:
            def cl = new RESTClient('https://prediction-engines.dev.clarabridge.net:8443/')
            cl.defaultRequestHeaders['Authorization'] = 'Bearer ZGV2OjEyMzQ1'
            def encoder = cl.getEncoder()
            //def encoder = new EncoderRegistry()

            def entity = encoder.encodeJSON([
                words: ['phone', 'phones'],
                suggesterName: 'Default',
                aggregationMode: 'CENTRALIZED',
                top: '2',
                communityTop: '2'
            ], null)

            String json = entity.content.text

            def resp = cl.post(
                path: 'v1/csls/suggestSimilarWords',
                contentType: ContentType.JSON,
                body : json)

        then:
            //json == ''
            //json == '["phone","phones"]'
            resp.status == 200
    }
}
