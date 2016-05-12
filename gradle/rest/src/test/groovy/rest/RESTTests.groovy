package rest

import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import groovyx.net.http.EncoderRegistry

//import net.sf.json.JSON
//import net.sf.json.JSONArray

import groovyx.gpars.GParsExecutorsPool

import org.apache.http.HttpResponse

import spock.lang.Specification
//import spock.lang.Unroll

class RESTTests extends Specification {
    def suggestSimilarWords() {
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
        cl.post(
            path: 'v1/csls/suggestSimilarWords',
            contentType: ContentType.JSON,
            body: json)
    }

    def assertResp(HttpResponse resp) {
        assert 200 == resp.status
        resp.data == [
            responseHash: '1c8f2d18950224cceccb65f61f5342774b9a67c5a62044dcde2dbecd1f6ccf28',
            suggestedWords: [
                [name: 'CELL PHONE', similarity:0.779014554325182],
                [name:'CELLPHONE', similarity:0.7722717367639094]
            ]
        ]
    }

    def "test rest"() {
        when:
            def resp = suggestSimilarWords()

            GParsExecutorsPool.withPool(12) {
                (1..1000).eachParallel { // parallel.each
                    def itResp = suggestSimilarWords()
                    assertResp(itResp)
                }
            }
        then:
            assertResp(resp)
    }
}
