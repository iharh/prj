package com.clarabridge.clbfoma;

import jnr.ffi.LibraryLoader;

public class ClbFomaLoader {
    private static final String LIB_NAME = "clbfoma";

    public static LibClbFoma load() {
        return LibraryLoader.create(LibClbFoma.class).load(LIB_NAME);
    }
}
