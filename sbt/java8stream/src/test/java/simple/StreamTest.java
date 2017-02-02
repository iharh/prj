package simple;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamTest {
    private static final Logger log = LoggerFactory.getLogger(StreamTest.class);

    static class WordAndVector {
        private String word;
        private int id;

        public WordAndVector(String word, int id) {
            this.word = word;
            this.id = id;
        }
        public String getWord() {
            return word;
        }
        public int getId() {
            return id;
        }
    }

    private static List<WordAndVector> sampleList() {
        WordAndVector t1 = new WordAndVector("tok1", 1);
        WordAndVector t2 = new WordAndVector("tok2", 2);

        List<WordAndVector> result = new ArrayList<WordAndVector>();
        result.add(t1);
        result.add(t2);

        return result;
    }

    @Test
    public void testSimple() throws Exception {
        log.info("simple");

        List<WordAndVector> tokens = sampleList();

        String tokenNames = tokens.stream().map(WordAndVector::getWord).collect(Collectors.joining(","));
        assertEquals("tok1,tok2", tokenNames);

        assertTrue(true);
    }
}
