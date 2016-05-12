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
            def encoder = cl.getEncoder() // = new EncoderRegistry()
            def entity = encoder.encodeJSON([
                words: ['phone', 'phones'],
                suggesterName: 'Default',
                aggregationMode: 'CENTRALIZED',
                top: '2',
                communityTop: '2'
            ], null)
            def json = entity.content.text
            def resp = cl.post(
                path: 'v1/csls/suggestSimilarWords',
                contentType: ContentType.JSON,
                body: json)
        then:
            resp.status == 200
            resp.data == [
                responseHash: '1c8f2d18950224cceccb65f61f5342774b9a67c5a62044dcde2dbecd1f6ccf28',
                suggestedWords: [
                    [name: 'CELL PHONE', similarity:0.779014554325182],
                    [name:'CELLPHONE', similarity:0.7722717367639094]
                ]
            ]
    }
}
