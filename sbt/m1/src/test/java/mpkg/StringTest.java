package mpkg;

import org.junit.Test;
import org.junit.Ignore;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import org.springframework.util.StringUtils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class StringTest {
    @Test
    public void testString() throws Exception {
        Set<String> expected = new HashSet<String>();

        expected.add("aaa");
        expected.add("bbb");
        expected.add("ccc");

        String [] words = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray("aaa, bbb, ccc"));

        assertThat(expected, containsInAnyOrder(words));
    }
};
