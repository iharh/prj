package org.gradle.internal.resource.transport.http;

import org.gradle.internal.resource.ReadableContent;
import org.gradle.internal.resource.transfer.ExternalResourceUploader;

import org.gradle.internal.impldep.org.apache.http.client.methods.HttpPut;
import org.gradle.internal.impldep.org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;

public class MyHttpResourceUploader implements ExternalResourceUploader {

    private final MyHttpClientHelper http;

    public MyHttpResourceUploader(MyHttpClientHelper http) {
        this.http = http;
    }

    @Override
    public void upload(ReadableContent resource, URI destination) throws IOException {
        HttpPut method = new HttpPut(destination);
        final RepeatableInputStreamEntity entity = new RepeatableInputStreamEntity(resource, ContentType.APPLICATION_OCTET_STREAM);
        method.setEntity(entity);
        try (HttpClientResponse response = http.performHttpRequest(method)) {
            if (!response.wasSuccessful()) {
                URI effectiveUri = response.getEffectiveUri();
                throw new HttpErrorStatusCodeException(response.getMethod(), effectiveUri.toString(), response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            }
        }
    }
}
