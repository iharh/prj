package com.clarabridge.cld2;

import jnr.ffi.annotations.Encoding;
import jnr.ffi.annotations.Synchronized;

public interface LibCld2 {
    @Encoding("UTF-8")
    int detectLangClb(String text);
    @Synchronized
    int getMemoryUsage(); // TODO: use underlying OS-level functions directly via loading libc/kernel32
}
