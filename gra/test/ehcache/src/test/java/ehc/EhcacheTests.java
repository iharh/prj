package ehc;

import org.junit.jupiter.api.Test;

//import org.ehcache.impl.internal.concurrent.ThreadLocalRandomUtil;

import sun.misc.Unsafe;

import static org.assertj.core.api.Assertions.assertThat;

public class EhcacheTests {
    private static final Unsafe U = ThreadLocalRandomUtil.UNSAFE;

    @Test
    void testEhcache() throws Exception {
        assertThat(U).isNotNull();
    }
}
