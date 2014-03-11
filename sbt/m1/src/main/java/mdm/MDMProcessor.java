package mdm;

import mdm.in.InDoc;
import mdm.in.InSeg;
import mdm.out.OutDoc;
import mdm.out.OutSeg;
import mdm.out.Word;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import org.apache.http.entity.InputStreamEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.utils.URIBuilder;
// http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/HttpClientBuilder.html
import java.net.URI;
import java.net.URISyntaxException;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.http.entity.ContentType.*;

import static java.nio.charset.StandardCharsets.*;

public class MDMProcessor {
    private static final Logger log = LoggerFactory.getLogger(MDMProcessor.class);
    
    private static final String HTTP_SCHEME = "http";
    private static final String HTTP_HOST = "localhost";
    private static final int HTTP_PORT = 8223;

    private static final int REQ_SOCKET_TIMEOUT = 20000;
    private static final int REQ_CONNECT_TIMEOUT = 20000;

    private static final String DOC_ID_PREFIX = "docid";
    private static final String SEG_ID_PREFIX = "segid";

    private static long refCnt;

    private static CloseableHttpClient mdmHttpClient;
    private static URI mdmURI;
    private static RequestConfig mdmRequestConfig;

    private static synchronized CloseableHttpClient getHttpClient() throws IOException{
        if (mdmHttpClient == null) {
            mdmHttpClient = HttpClients.createDefault();

            try {
                mdmURI = new URIBuilder()
                    .setScheme(HTTP_SCHEME)
                    .setHost(HTTP_HOST)
                    .setPort(HTTP_PORT)
                    //.setPath("/search")
                    //.setParameter("q", "value")
                    .build();
            } catch (URISyntaxException e) {
                throw new IOException(e.getMessage(), e);
            }

            mdmRequestConfig = RequestConfig.custom()
                .setSocketTimeout(REQ_SOCKET_TIMEOUT)
                .setConnectTimeout(REQ_CONNECT_TIMEOUT)
                .build();
        }
        return mdmHttpClient;
    }

    public static synchronized void init() throws IOException {
        ++refCnt;
        if (refCnt == 1) {
            getHttpClient(); // !!! side-effect is mdmURI initialization !!!
        }
    }

    public static synchronized void release() throws IOException {
        if (refCnt > 0)
            --refCnt;
        if (refCnt == 0 && mdmHttpClient != null) {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            // mdmHttpClient.getConnectionManager().shutdown();
            mdmHttpClient.close();
            mdmHttpClient = null;
        }
    }

    public static synchronized void process(InputStream in, OutputStream out) throws IOException {
        CloseableHttpClient httpclient = mdmHttpClient; 

        InputStreamEntity reqEntity = new InputStreamEntity(in, APPLICATION_XML);
        reqEntity.setChunked(true);

        HttpPost httppost = new HttpPost(mdmURI);
        httppost.setConfig(mdmRequestConfig);
        httppost.setEntity(reqEntity);

        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            HttpEntity resEntity = response.getEntity();
            resEntity.writeTo(out);
            EntityUtils.consume(resEntity); // we can use toString(resEntity) also
        }
    }

    public static OutDoc process(InDoc inDoc) throws IOException, ClassNotFoundException {
        // TODO: probably this is the best place to optimize
        // 1. XStream performance
        //   One approach you could try is to create your own implementations of the HierarchicalStreamWriter and HierarchicalStreamReader interfaces that make use of NIO.
        //   These are the bridges between the tree structurethat XStream works with and the underlying representation.
        //   OR.... If this is a bit daunting, you could just create OutputStream/InputStream wrappers around ByteBuffers. This will at least save the intermediate conversions.
        //
        // Try to use Netty in order to support puuled circular input-output buffers in order to avoid GC stress and extra memory copying
        //   http://netty.io/
        //   https://blog.twitter.com/2013/netty-4-at-twitter-reduced-gc-overhead
        //     http://netty.io/4.0/api/io/netty/buffer/ByteBufAllocator.html
        //
        // Memory allocation:
        // Use cache of ByteBuffer-s with flip to swith from write to read (clear/compact to switch back from write to read)
        // ... byteBuffer.asCharBuffer()...
        // ... ByteBuf/CompositeByteBuf/ByteBufAllocator and other stuff from ???
        //       this is ref-countable, resizeable, separate idx for read/write, fluent i-face
        //
        //     ChannelHandler[Inbound/Outbound/State/Operation] and ChannelPipeline/ChannelPipelineFactory
        //       always called by assigned EventExecutor (or default one), typesafe
        //         ScheduledEventExecutor <= EventExecutor <= EventLoop
        //     in handler - don't block, byt call ctx.executor().schedule(... task ...);
        //
        // ??  ChannelAdopter[Inboud/Outbound]
        // ??  Encoders, Decoders, Hybrids (ByteToByteCodec, ByteToMessage, MessageToMessage)
        // ??  Boostrap, EventLoopGroup
        //
        // Try to look at NIO Zero-Copy and Direct-Channel-Transfer technics
        // ... XXInputStream -> getChannel -> transferTo
        // Readable/Writeable-ByteChannel
        // ?? Selectors - Multiplexed IO, onRead/onWrite
        ByteArrayInputStream bis = null;
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) { // TODO: specifying direct size impacts performance a lot !!!
                MDMConverter.marshal(inDoc,
                    new BufferedWriter(
                        new OutputStreamWriter(bos, UTF_8)
                    )
                );
                bis = new ByteArrayInputStream(bos.toByteArray());
            }
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) { // TODO: specifying direct size impacts performance a lot !!!
                process(bis, bos);

                bis.close();
                bis = new ByteArrayInputStream(bos.toByteArray());
            }
            OutDoc outDoc = MDMConverter.unmarshal(
                new BufferedReader(
                    new InputStreamReader(bis, UTF_8)
                )
            );
            computeOffsets(inDoc, outDoc);

            return outDoc;
        } finally {
            if (bis != null)
                bis.close();
        }
    }

    private static void computeOffsets(InDoc inDoc, OutDoc outDoc) {
        //log.debug("computeOffsets called");
        if (inDoc == null || outDoc == null) {
            return;
        }
        InSeg [] inSegs = inDoc.getSegs();
        OutSeg [] outSegs = outDoc.getSegs();

        if (inSegs == null || outSegs == null) {
            return;
        }
        int segNum = Math.min(inSegs.length, outSegs.length);

        for (int i = 0; i < segNum; ++i) {
            InSeg inSeg = inSegs[i];
            OutSeg outSeg = outSegs[i];
            if (inSeg == null || outSeg == null) {
                continue;
            }
            String value = inSeg.getValue();
            Word [] outWords = outSeg.getWords();
            if (value == null || outWords == null) {
                continue;
            }
            int curIdx = 0;

            for (Word outWord : outWords) {
                if (outWord == null) {
                    continue;
                }
                String word = outWord.getWord();
                if (word == null) {
                    continue;
                }
                int idx = value.indexOf(word, curIdx);
                if (idx >= 0) {
                    outWord.setWordStartPos(idx);
                    curIdx = idx;
                } else {
                    outWord.setWordStartPos(curIdx);
                    //log.debug("computeOffsets - unknown idx for: {}", word);
                }
                curIdx += word.length();
            }
        }
    }

    public static Word [] processText(String text) throws IOException, ClassNotFoundException {
        final String DEFAULT_ID = "1";

        InSeg [] inSegs = new InSeg[1];
        inSegs[0] = new InSeg(SEG_ID_PREFIX + DEFAULT_ID, text);
        InDoc inDoc = new InDoc(DOC_ID_PREFIX + DEFAULT_ID, inSegs);

        OutDoc outDoc = process(inDoc);

        if (outDoc == null) {
            return null;
        }
        
        OutSeg [] outSegs = outDoc.getSegs();

        if (outSegs == null) {
            return null;
        }

        OutSeg outSeg = outSegs[0];

        return outSeg == null ? null : outSeg.getWords();
    }
};

