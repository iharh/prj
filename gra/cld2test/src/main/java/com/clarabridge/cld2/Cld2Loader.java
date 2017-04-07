package com.clarabridge.cld2;

import jnr.ffi.LibraryLoader;

public class Cld2Loader {
    private static final String LIB_NAME = "cld2";

    public static LibCld2 load() {
        return LibraryLoader.create(LibCld2.class).load(LIB_NAME);
    }
}
