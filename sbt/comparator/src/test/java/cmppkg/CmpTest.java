package cmppkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

//import org.hamcrest.Matcher;
//import org.hamcrest.CoreMatchers;

//git://github.com/hamcrest/JavaHamcrest.git
//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.collection.IsArrayContainingInAnyOrder.*;
//import static org.hamcrest.core.IsCollectionContaining.*;

import org.junit.Test;
//import org.junit.Ignore;
//import org.junit.Before;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import java.util.Random;

public class CmpTest {
	@Test
	public void testCmp() throws Exception {
		// List<String> col = new ArrayList<String>(Arrays.asList(new String[]{"bbb", "eee", "aaa", "ddd", "ccc"}));
		final Random rand = new Random();

		final int col_size = 40000000;
		List<Integer> col = new ArrayList<Integer>(col_size);
		for (int i = 0; i < col_size; ++i) {
			boolean isForceOne = rand.nextInt(4) == 0;
			//assertTrue(isForceOne);
			col.add(isForceOne ? -1 : i);
		}
		assertNotNull(col);
		//assertEquals(3, col.size());

		//int result = rand.nextInt(3) - 1;

		Collections.sort(col, new Comparator<Integer>() {
			private int cmp(int i1, int i2) {
				if (i1 < i2)
					return -1;
				if (i1 > i2)
					return  1;
				return 0;
			}

			private boolean isForceOne(int i1, int i2) {
				//return (i1 & 1) > 0 || (i2 & 1) > 0;
				return i1 == -1 || i2 == -1;
			}

			@Override
			public int compare(Integer o1, Integer o2) {
				int i1 = o1.intValue();
				int i2 = o2.intValue();

				if (isForceOne(i1, i2))
					return 1;

				int result = cmp(i1, i2);
				//System.out.println("compare " + i1 + " with " + i2 + " : " + result);
				return result;
			}
		});
		

		//fail("My msg");
		//
		//assertEquals(7, speakers.length);
		//final List<String> list = Arrays.asList(speakers);
		//assertThat(Arrays.asList(speakers), hasItems("aaa", "bbb"));
		//assertThat(speakers, arrayContainingInAnyOrder("aaa", "bbb"));

		//byte b = 0x33;
		//assertTrue((b & 0x01) != 0);
		//assertTrue((b & 0x02) != 0);
	}
};
