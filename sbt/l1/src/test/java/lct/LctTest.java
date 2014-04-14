package lct;

import org.junit.Test;
import org.junit.Ignore;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LctTest {

    private static String dumpFieldTokens(Fields fields, String fieldName) throws IOException {
        StringBuilder result = new StringBuilder();
        Terms terms = fields.terms(fieldName);
        TermsEnum termsEnum = terms.iterator(null);
        while (termsEnum.next() != null) {
            BytesRef br = termsEnum.term();
            result.append("\n").append(br.utf8ToString());
        }
        return result.toString();
    }

    @Test
    public void test1() throws Exception {
        final String idxLocation = "D:\\dev\\PL\\java\\jtools\\clue\\0";

        Directory dir = FSDirectory.open(new File(idxLocation));

        if (!DirectoryReader.indexExists(dir)){
            System.out.println("lucene index does not exist at: "+idxLocation);
            assertTrue(false);
        }

        IndexReader r = DirectoryReader.open(dir);

        Fields fields = MultiFields.getFields(r);

        String d = dumpFieldTokens(fields, "_mstokenname"); // _words, _verbatim

        r.close();

        assertEquals("", d);
    }
}
