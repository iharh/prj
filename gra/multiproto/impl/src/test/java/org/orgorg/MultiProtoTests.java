package org.orgorg;

import org.junit.jupiter.api.Test;

import org.someorg.serialization.proto.MPService;

import com.google.protobuf.util.JsonFormat;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiProtoTests {
    @Test
    public void test1() throws Exception {
        MPService.MPResponse.Builder respBuilder = MPService.MPResponse.newBuilder();
        respBuilder.setRes("abc");
        MPService.MPResponse resp = respBuilder.build();
        assertThat(resp).isNotNull();

        String content = JsonFormat.printer().includingDefaultValueFields().print(resp);
        assertThat(content).isEqualTo("{\n  \"res\": \"abc\"\n}");
    }
}
