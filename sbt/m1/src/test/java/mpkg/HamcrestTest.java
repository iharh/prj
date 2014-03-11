package mpkg;

import org.junit.Test;
import org.junit.Ignore;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class HamcrestTest {
    @Ignore
    public void testHamcrest() throws Exception {
        Set<String> s1 = new HashSet<String>();
        Set<String> s2 = new HashSet<String>();

        s1.add("red");
        s1.add("green");
        s1.add("orange");

        s2.add("green");
        s2.add("orange");
        s2.add("red");

        assertThat(s1, containsInAnyOrder(s2.toArray()));

        List<String> l1 = new ArrayList<String>(1);
        l1.add("abc");

        assertThat(l1, hasItem("abc"));
    }
};
