package csvstream;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.csvreader.CsvWriter;

import rx.Observable;
import rx.Observer;
import rx.observables.ConnectableObservable;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


import java.util.List;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.*;

public class RxTest {

	private class StatItem {
		private int id;
		private double freq;

		public StatItem(int id, double freq) {
			this.id = id;
			this.freq = freq;
		}

		public void write(CsvWriter w) throws IOException {
			w.write(Integer.toString(id));
			w.write(Double.toString(freq));
			w.endRecord();
		}
	}

	private class StatWriteObserver implements Observer<StatItem> {
		private CsvWriter w;

		public StatWriteObserver(CsvWriter w) throws IOException {
			this.w = w;
			w.writeRecord(new String [] {"RECORD_ID", "FREQ"});
		}

		@Override
		public void onNext(StatItem item) {
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

	private Observer<StatItem> createStatWriteObserver(String name) throws IOException, FileNotFoundException {
		return new StatWriteObserver(new CsvWriter(createOutWriter("D:\\" + name + ".csv"), ','));
	}

	@Test
	public void test1() throws Exception {
		final int IDS_SIZE = 7;
		List<StatItem> items = new ArrayList<StatItem>(IDS_SIZE);
		for (int i = 0; i < IDS_SIZE; ++i)
			items.add(new StatItem(i, i + 1));

		ConnectableObservable<StatItem> o = Observable.from(items).publish();
		o.subscribe(createStatWriteObserver("w1"));
		o.subscribe(createStatWriteObserver("w2"));
		o.connect();

		PrintWriter wt = new PrintWriter(createOutWriter("D:\\wt.txt"));
		wt.print("a"); wt.print("b"); wt.println();
		wt.println("def");
		wt.close();

		assertTrue(true);
    	}
}

