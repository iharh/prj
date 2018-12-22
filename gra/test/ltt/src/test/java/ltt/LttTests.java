package ltt;

import org.junit.jupiter.api.Test;

import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import static java.nio.charset.StandardCharsets.*;

public class LttTests {
    // http.scheme=http
    // http.host=localhost
    // http.port=8091
    // analyze.rest.path=/analyze
    // req.socket.timeout=20000
    // req.connect.timeout=20000

    private JsonFactory factory = new JsonFactory();
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpHost lttHttpHost = new HttpHost("localhost", 8091, "http");

    @Test
    void testPost() throws Exception {
        CloseableHttpClient lttHttpClient = HttpClients.createDefault();

        HttpPost httpMethod = createHttpMethod("def{ 1 } \"aaa\"");
        try (CloseableHttpResponse lttHttpResponse = lttHttpClient.execute(lttHttpHost, httpMethod)) {
            StatusLine lttResponseStatusLine = lttHttpResponse.getStatusLine();
            assertThat(lttResponseStatusLine.getStatusCode()).isEqualTo(200);
        
            HttpEntity lttResponseEntity = lttHttpResponse.getEntity();

            String data = getData(lttResponseEntity);
            assertThat(data).isEqualTo("CgcaBWRlZnsgChgIAhAFGgExIg8KATESCvz//////////wEKDRAGGgkgfSAiYWFhIiA=");
        }
    }

    private HttpPost createHttpMethod(String text) throws IOException {
        RequestConfig lttRequestConfig = createRequestConfig();
        URI lttURI = createURI();

        HttpPost httpMethod = new HttpPost(lttURI);
        httpMethod.setConfig(lttRequestConfig);

        String jsonRequest;
        try (ByteArrayOutputStream bosRequest = new ByteArrayOutputStream()) { // TODO: specifying direct size impacts performance a lot !!!
            try (JsonGenerator generator = factory.createGenerator(bosRequest, JsonEncoding.UTF8)) {
                generator.writeStartObject();
                generator.writeFieldName("text");
                generator.writeString(text);
                generator.writeEndObject();
            } // generator.close();

            jsonRequest = bosRequest.toString(UTF_8.name());
            //bis = new ByteArrayInputStream(bos.toByteArray());
            //log.debug("bis: {}", bos.toString());
        }

        httpMethod.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));
        return httpMethod;
    }

    private String getData(HttpEntity lttResponseEntity) throws IOException {
        String result;
        // long lttRespLen = lttResponseEntity.getContentLength();
        // assertThat(lttRespLen).isEqualTo(-1);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) { // (int)lttRespLen: specifying direct size impacts performance a lot
            lttResponseEntity.writeTo(bos);
            String responseContent = bos.toString(UTF_8.name());
            // assertThat(responseContent).isNotNull();
            // System.out.println("responseContent: " + responseContent);
            JsonNode node = objectMapper.readValue(responseContent, JsonNode.class);

            JsonNode dataNode = node.get("data");
            result = dataNode.asText();
        }
        EntityUtils.consume(lttResponseEntity);
        return result;
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
            .setSocketTimeout(20000)
            .setConnectTimeout(20000)
            .build();
    }

    private URI createURI() throws IOException {
        try {
            return new URIBuilder()
                // This stuff is already at lttHttpHost:
                //      .setScheme(propHttpScheme)
                //      .setHost(propHttpHost)
                //      .setPort(propHttpPort)
                .setPath("/process")
                .build();
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
