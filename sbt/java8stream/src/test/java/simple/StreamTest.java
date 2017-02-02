package simple;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.util.stream.IntStream;
import java.util.stream.Collectors;

import javafx.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamTest {
    private static final Logger log = LoggerFactory.getLogger(StreamTest.class);

    static public class WordAndVector extends Pair<String, double[]> {
        public WordAndVector(String key, double[] value) {
            super(key, value);
        }
        public String getWord() {
            return getKey();
        }
        public double[] getVector() {
            return getValue();
        }
        @Override
        public String toString() {
            return String.format("{ word: %s }", getWord());
        }
    }

    private static List<WordAndVector> sampleList(int numTokens) {
        return IntStream.rangeClosed(1, numTokens)
            .mapToObj(i -> {
                return new WordAndVector("tok" + i, null);
            })
            .collect(Collectors.toList());
    }

    private List<List<WordAndVector>> toCommunitiesList(List<WordAndVector> tokens) {
        return tokens.stream()
            .map(token -> {
                return Arrays.asList(new WordAndVector(token.getWord(), token.getVector()));
            })
            .collect(Collectors.toList());
    }

    @Test
    public void testSimple() throws Exception {
        log.info("simple");

        int numTokens = 3;
        List<WordAndVector> tokens = sampleList(numTokens);

        String tokenNames = tokens.stream().map(WordAndVector::getWord).collect(Collectors.joining(","));
        assertEquals("tok1,tok2,tok3", tokenNames);

        List<List<WordAndVector>> commmunities = toCommunitiesList(tokens);
        assertEquals(numTokens, commmunities.size());

        IntStream.rangeClosed(1, numTokens)
            .forEach(i -> {
                List<WordAndVector> comm1 = commmunities.get(i-1);
                assertEquals(1, comm1.size());
                assertEquals("tok" + i, comm1.get(0).getWord());
            });
    }
}
