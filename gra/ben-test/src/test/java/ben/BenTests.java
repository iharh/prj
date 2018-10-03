package ben;

import org.junit.jupiter.api.Test;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

//import org.apache.http.entity.InputStreamEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.utils.URIBuilder;

// http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/HttpClientBuilder.html
import java.net.URI;
import java.net.URISyntaxException;

/*
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
*/
import java.io.IOException;

// import static org.apache.http.entity.ContentType.*;

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

    /*
    public static synchronized void process(InputStream in, OutputStream out) throws IOException {
        CloseableHttpClient httpclient = mdmHttpClient; 

        InputStreamEntity reqEntity = new InputStreamEntity(in, APPLICATION_XML);
        reqEntity.setChunked(true);

        HttpPost httppost = new HttpPost(mdmURI);
        httppost.setConfig(mdmRequestConfig);
        httppost.setEntity(reqEntity);

        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            HttpEntity resEntity = response.getEntity();

            //resEntity.writeTo(out);
            EntityUtils.consume(resEntity); // we can use toString(resEntity) also
        }
    }
    */

    @Test
    void justAnExample() throws Exception {
        RequestConfig lttRequestConfig = createRequestConfig();
        assertNotNull(lttRequestConfig);
        URI lttURI = createURI("abc");
        assertNotNull(lttURI);

        try (CloseableHttpClient lttHttpClient = HttpClients.createDefault()) {
            assertNotNull(lttHttpClient);

            HttpGet httpget = new HttpGet(lttURI);
            httpget.setConfig(lttRequestConfig);

            try (CloseableHttpResponse lttHttpResponse = lttHttpClient.execute(httpget)) { // client.execute(..., HttpContext)
                StatusLine lttResponseStatusLine = lttHttpResponse.getStatusLine();
                assertNotNull(lttResponseStatusLine);
                assertEquals(HttpStatus.SC_OK, lttResponseStatusLine.getStatusCode());

                HttpEntity lttResponseEntity = lttHttpResponse.getEntity();
                assertNotNull(lttResponseEntity);
                    
                EntityUtils.consume(lttResponseEntity); // we can use toString(resEntity) also
            }
        }
    }
}
