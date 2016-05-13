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
    def suggestSimilarWords(String langCode, List words) {
        def cl = new RESTClient('https://prediction-engines.dev.clarabridge.net:8443/')
        cl.defaultRequestHeaders['Authorization'] = 'Bearer ZGV2OjEyMzQ1'
        def encoder = cl.getEncoder() // = new EncoderRegistry()
        def entity = encoder.encodeJSON([
            lang: langCode,
            words: words,
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

    def enSSW() { suggestSimilarWords('en', ['phone', 'phones']) }
    def deSSW() { suggestSimilarWords('de', ['hoh']) }
    def frSSW() { suggestSimilarWords('fr', ['lamour']) }
    def esSSW() { suggestSimilarWords('es', ['honda']) }

    def assertResp(HttpResponse resp, String hash, List words) {
        assert 200 == resp.status
        resp.data == [
            responseHash: hash,
            suggestedWords: words
        ]
    }

    def enAssert(HttpResponse resp) { assertResp(resp,
        '1c8f2d18950224cceccb65f61f5342774b9a67c5a62044dcde2dbecd1f6ccf28',
        [
            [ name: 'CELL PHONE', similarity: 0.779014554325182 ],
            [ name: 'CELLPHONE' , similarity: 0.7722717367639094]
        ])}
    def deAssert(HttpResponse resp) { assertResp(resp,
        '4a66aafeaf46a92d37d4d8ec067fe4773262201afaaf42e3f18405e8b8075115',
        [
            [ name: 'KLEINWEILAND', similarity: 0.5147832573493378],
            [ name: 'MMTZ'        , similarity: 0.5131634794648856]
        ])}
    def frAssert(HttpResponse resp) { assertResp(resp,
        '822eb839cd6b0fc667841295616b6090103abf699e241c7444ad87b6c1dab0f1',
        [
            [ name: 'DAMOUR' , similarity: 0.7053097337264178],
            [ name: 'L’AMOUR', similarity: 0.0               ]
        ])}
    def esAssert(HttpResponse resp) { assertResp(resp,
        '08c495f642e2a7da1ca37260090ccd5073c92fc8b7aea9eaed0f6cb4b791acaf',
        [
            [ name: 'PROFUNDA' , similarity: 0.7073650296815003],
            [ name: 'EXPRESIVA', similarity: 0.6827207931351051]
        ])}

    def "test rest"() {
        when:
            def resp = esSSW()
            GParsExecutorsPool.withPool(12) {
                (1..1000).eachParallel { // parallel.each
                    def itResp = esSSW()
                    esAssert(itResp)
                }
            }
        then:
            esAssert(resp)
    }
}
