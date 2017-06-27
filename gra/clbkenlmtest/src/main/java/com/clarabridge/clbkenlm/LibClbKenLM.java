package com.clarabridge.clbkenlm;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.Encoding;
import jnr.ffi.types.size_t;

public interface LibClbKenLM {
    Pointer kenlm_init(@size_t int size, byte [] data);

    void kenlm_clean(Pointer pHandle);

    @Encoding("UTF-8")
    float kenlm_query(Pointer pHandle, String tag);
}
