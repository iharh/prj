package csvstream;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.csvreader.CsvWriter;

//import rx.Observable;
import rx.Observer;
//import rx.observables.ConnectableObservable;
import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.byLine;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;


//import java.util.List;
//import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.*;

public class DictTest {

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
        //final int IDS_SIZE = 7;
        //List<StatItem> items = new ArrayList<StatItem>(IDS_SIZE);
        //for (int i = 0; i < IDS_SIZE; ++i)
        //	items.add(new StatItem(i, i + 1));

        //ConnectableObservable<StatItem> o = Observable.from(items).publish();
        //o.subscribe(createStatWriteObserver("w1"));
        //o.subscribe(createStatWriteObserver("w2"));
        //o.connect();

        //final List<String> lines = byLine(
        //    from(new FileReader("en-words-100.txt"))
        //)
        //.toList()
        //.toBlocking()
        //.single();

        assertTrue(true);
    }
}

