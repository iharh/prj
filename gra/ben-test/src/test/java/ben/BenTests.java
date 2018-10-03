package ben;

import org.junit.jupiter.api.Test;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BenTests {
    private static final String HTTP_SCHEME = "http";
    private static final String HTTP_HOST = "localhost";
    private static final String HTTP_PATH = "/analyze";
    private static final int HTTP_PORT = 8080;

    private static final int REQ_SOCKET_TIMEOUT = 20000;
    private static final int REQ_CONNECT_TIMEOUT = 20000;

    private static URI createURI(String text) throws IOException{
        try {
            return new URIBuilder()
                .setScheme(HTTP_SCHEME)
                .setHost(HTTP_HOST)
                .setPort(HTTP_PORT)
                .setPath(HTTP_PATH)
                .setParameter("text", text)
                .build();
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
    
    private static RequestConfig createRequestConfig() {
        return RequestConfig.custom()
            .setSocketTimeout(REQ_SOCKET_TIMEOUT)
            .setConnectTimeout(REQ_CONNECT_TIMEOUT)
            .build();
    }

    @Test
    void justAnExample() throws Exception {
        RequestConfig lttRequestConfig = createRequestConfig();
        assertNotNull(lttRequestConfig);
        URI lttURI = createURI("খালিদ  জামিলকে দেখে মনে হচ্ছে খাঁচাবন্দি ‘বাঘ’..!");
        assertNotNull(lttURI);

        try (CloseableHttpClient lttHttpClient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(lttURI);
            httpget.setConfig(lttRequestConfig);
            try (CloseableHttpResponse lttHttpResponse = lttHttpClient.execute(httpget)) { // client.execute(..., HttpContext)
                StatusLine lttResponseStatusLine = lttHttpResponse.getStatusLine();
                assertEquals(HttpStatus.SC_OK, lttResponseStatusLine.getStatusCode());

                HttpEntity lttResponseEntity = lttHttpResponse.getEntity();
                long lttRespLen = lttResponseEntity.getContentLength();
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int)lttRespLen)) { // specifying direct size impacts performance a lot
                    lttResponseEntity.writeTo(bos); 

                    String expectedRes = "CgYIARoCICAKYggCEAcaFeCmnOCmvuCmruCmv+CmsuCmleCnhyJFCg/gppzgpr7gpq7gpr/gprISMv7//////////wHz//////////8B4P//////////Adf//////////wHT//////////8BCgUQDhoBIAojIiEKCeCmpuCnh+CmlhIU9v//////////AcH//////////wEKNyI1Cgngpqbgp4fgppYSKPb//////////wHH//////////8ByP//////////Ac3//////////wEKUwgCEA8aDOCmpuCnh+CmluCnhyI/Cgngpqbgp4fgppYSMvb//////////wHH//////////8ByP//////////Ac7//////////wHi//////////8BCgUQExoBIApNCAIQFBoJ4Kau4Kao4KeHIjwKBuCmruCmqBIy////////////Ad7//////////wHc//////////8B1///////////AdH//////////wEKBRAXGgEgCjEiLwoD4Ka5Eij2//////////8Bx///////////Acb//////////wHN//////////8BClAIAhAYGg/gprngpprgp43gppvgp4ciOQoD4Ka5EjL2//////////8Bx///////////Acb//////////wHO//////////8B4v//////////AQoFEB0aASAKCggBEB4aBCDigJgKUAgCECoaCeCmrOCmvuCmmCI/Cgngpqzgpr7gppgSMv///////////wHg//////////8B2///////////Adf//////////wHU//////////8BCgcQLRoD4oCZChgIAhAuGgEuIg8KAS4SCr///////////wEKGAgCEC8aAS4iDwoBLhIKv///////////AQ==";
                    assertEquals(expectedRes, bos.toString()); // bos.toByteArray()
                } 
                EntityUtils.consume(lttResponseEntity); // we can use toString(resEntity) also
            }
        }
    }
}
