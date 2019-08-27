package org.orgorg;

import org.junit.jupiter.api.Test;

import org.someorg.serialization.proto.MPService;

import com.google.protobuf.util.JsonFormat;
import com.google.common.io.BaseEncoding;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiProtoTests {
    @Test
    public void test1() throws Exception {
        MPService.MPResponse.Builder respBuilder = MPService.MPResponse.newBuilder();
        respBuilder.setRes("When's");
        MPService.MPResponse resp = respBuilder.build();
        assertThat(resp).isNotNull();

        String encodedContent = encodeBase64(resp);
        assertThat(encodedContent).isEqualTo("CgZXaGVuJ3M=");

        String jsonContent = JsonFormat.printer().includingDefaultValueFields().print(resp);
        assertThat(jsonContent).isEqualTo("{\n  \"res\": \"When\\u0027s\"\n}");

        System.out.println("jsonContent :" + jsonContent);
    }

    private String encodeBase64(MPService.MPResponse res) {
        byte[] byteArray = res.toByteArray();
        String output = BaseEncoding.base64().encode(byteArray);
        return output;
    }
}
