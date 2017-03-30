import junit.framework.TestCase;

import jnr.ffi.LibraryLoader;

public class MyTest extends TestCase {
    public static interface LibCld2 {
        int puts(String s);
    }

    public void testMy() throws Exception {
        final String libName = "cld2-linux-1.0.0"; // -windows-
        LibCld2 cld2 = LibraryLoader.create(LibCld2.class).load(libName);
        assertNotNull(cld2);

        assertEquals(2, cld2.puts(""));
        //My obj = new My();
        //assertNotNull(obj);
    }
}
