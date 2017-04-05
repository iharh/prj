package com.clarabridge.cld2;

import jnr.ffi.LibraryLoader;

import org.apache.commons.lang3.SystemUtils;

public class Cld2Loader {
    private static final String CLD2_VER = "1.0.0";
    private static final String LIB_NAME = String.format("cld2-%s-%s", (SystemUtils.IS_OS_LINUX ? "linux" : "windows"), CLD2_VER);

    public static LibCld2 load() {
        return LibraryLoader.create(LibCld2.class).load(LIB_NAME);
    }
}
