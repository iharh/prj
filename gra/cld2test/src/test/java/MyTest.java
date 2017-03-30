import junit.framework.TestCase;

import jnr.ffi.LibraryLoader;

public class MyTest extends TestCase {
    public static interface LibCld2 {
        int puts(String s);
    }

    public void testMy() throws Exception {
        LibCld2 cld2 = LibraryLoader.create(LibCld2.class).load("cld2-windows-1.0.0");
        assertNotNull(cld2);

        assertEquals(1, cld2.puts(""));
        //My obj = new My();
        //assertNotNull(obj);
    }
}
