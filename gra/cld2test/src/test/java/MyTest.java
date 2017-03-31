import junit.framework.TestCase;

import jnr.ffi.LibraryLoader;

import org.apache.commons.lang3.SystemUtils;

public class MyTest extends TestCase {
    private static final String CLD2_VER = "1.0.0";

    public static interface LibCld2 {
        int detectLangClb(String text);
    }

    public void testMy() throws Exception {
        final String libName = String.format("cld2-%s-%s", (SystemUtils.IS_OS_LINUX ? "linux" : "windows"), CLD2_VER);
        LibCld2 cld2 = LibraryLoader.create(LibCld2.class).load(libName);
        assertNotNull(cld2);

        //assertEquals(0, cld2.detectLangClb("I know and like so much my round table"));
        assertEquals(4, cld2.detectLangClb("quatre petits enfants, trois filles malades"));
    }
}
