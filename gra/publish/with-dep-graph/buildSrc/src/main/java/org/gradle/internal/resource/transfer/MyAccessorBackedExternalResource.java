package org.gradle.internal.resource.transfer;

import org.gradle.api.Action;
import org.gradle.api.resources.ResourceException;
import org.gradle.internal.resource.ExternalResourceName;
import org.gradle.internal.resource.ExternalResourceWriteResult;
import org.gradle.internal.resource.ReadableContent;
import org.gradle.internal.resource.ResourceExceptions;

import org.gradle.internal.resource.transfer.ExternalResourceUploader;

import org.gradle.internal.impldep.com.google.common.io.CountingInputStream;
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class MyAccessorBackedExternalResource {
    private final ExternalResourceName name;
    private final ExternalResourceUploader uploader;

    public MyAccessorBackedExternalResource(ExternalResourceName name, ExternalResourceUploader uploader) {
        this.name = name;
        this.uploader = uploader;
    }

    public URI getURI() {
        return name.getUri();
    }

    public ExternalResourceWriteResult put(final ReadableContent source) throws ResourceException {
        try {
            CountingReadableContent countingResource = new CountingReadableContent(source);
            uploader.upload(countingResource, getURI());
            return new ExternalResourceWriteResult(countingResource.getCount());
        } catch (IOException e) {
            throw ResourceExceptions.putFailed(getURI(), e);
        }
    }

    private static class CountingReadableContent implements ReadableContent {
        private final ReadableContent source;
        private CountingInputStream instr;
        private long count;

        CountingReadableContent(ReadableContent source) {
            this.source = source;
        }

        @Override
        public InputStream open() throws ResourceException {
            if (instr != null) {
                count += instr.getCount();
            }
            instr = new CountingInputStream(source.open());
            return instr;
        }

        @Override
        public long getContentLength() {
            return source.getContentLength();
        }

        public long getCount() {
            return instr != null ? count + instr.getCount() : count;
        }
    }
}
