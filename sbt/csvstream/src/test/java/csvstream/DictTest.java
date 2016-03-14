package csvstream;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.csvreader.CsvWriter;

import rx.Observer;
import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.byLine;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static java.nio.charset.StandardCharsets.*;

public class DictTest {
    private static final Logger log = LoggerFactory.getLogger(DictTest.class);

    private class DictItem {
        private String word;

        public DictItem(String word) {
            this.word = word;
        }

        public void write(CsvWriter w) throws IOException {
            w.write(word);
            w.endRecord();
        }
    }

    private class WriteObserver implements Observer<DictItem> {
        private CsvWriter w;

        public WriteObserver(CsvWriter w) throws IOException {
            this.w = w;
            w.writeRecord(new String [] {"#WORD"});
        }

        @Override
        public void onNext(DictItem item) {
            try {
                item.write(w);
            }
            catch (IOException e) {
            }
        }

        @Override
        public void onCompleted() {
            w.close(); // ex-unsafe
        }

        @Override
        public void onError(Throwable t) {
        }
    }

    private Writer createOutWriter(String fileName) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), UTF_8));
    }

    //private Observer<DictItem> createDictWriteObserver(String name) throws IOException, FileNotFoundException {
    //    return new DictWriteObserver(new CsvWriter(createOutWriter("D:\\" + name + ".csv"), ','));
    //}

    @Test
    public void test1() throws Exception {
        //ConnectableObservable<StatItem> o = Observable.from(items).publish();
        //o.subscribe(createStatWriteObserver("w1"));
        //o.subscribe(createStatWriteObserver("w2"));
        //o.connect();

        byLine(
            from(new FileReader("dict/in1.txt"))
        )
        .filter(
            (line) -> { return !line.startsWith("#"); }
        )
        .subscribe(
            (line) -> { log.info(line); }
        );

        assertTrue(true);
    }
}

