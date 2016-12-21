package myjackson;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonTest {
    private static final Logger log = LoggerFactory.getLogger(JacksonTest.class);

    public static class RecursiveToStringStyle extends ToStringStyle {
        private static final long serialVersionUID = 1L;

        public RecursiveToStringStyle() {
            super();
        }

        @Override
        public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
            if (!ClassUtils.isPrimitiveWrapper(value.getClass()) &&
                !String.class.equals(value.getClass()) &&
                accept(value.getClass())) {
                buffer.append(ReflectionToStringBuilder.toString(value, this));
            } else {
                super.appendDetail(buffer, fieldName, value);
            }
        }

        @Override
        protected void appendDetail(final StringBuffer buffer, final String fieldName, final Collection<?> coll) {
            appendClassName(buffer, coll);
            appendIdentityHashCode(buffer, coll);
            appendDetail(buffer, fieldName, coll.toArray());
        }
        
        protected boolean accept(final Class<?> clazz) {
            return true;
        }
    }

    @Test
    public void testFileRepoList() throws Exception {
        CC cc = new CC();
        cc.cc = "cc";

        CB cb = new CB();
        cb.cb = "cb";
        cb.cc = cc;

        CA ca = new CA();
        ca.ca = "ca";
        ca.cb = cb;

        log.info("ca: {}", ReflectionToStringBuilder.toString(ca, new RecursiveToStringStyle()));
        assertNotNull(ca);
    }
}
