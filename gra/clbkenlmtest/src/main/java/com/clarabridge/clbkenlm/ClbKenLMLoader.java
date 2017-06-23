package com.clarabridge.clbkenlm;

import jnr.ffi.LibraryLoader;

public class ClbKenLMLoader {
    private static final String LIB_NAME = "clbkenlm";

    public static LibClbKenLM load() {
        return LibraryLoader.create(LibClbKenLM.class).load(LIB_NAME);
    }
}
