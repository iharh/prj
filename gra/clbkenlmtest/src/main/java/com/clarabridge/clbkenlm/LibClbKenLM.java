package com.clarabridge.clbkenlm;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.annotations.Encoding;
import jnr.ffi.types.size_t;

public interface LibClbKenLM {
    Pointer kenlm_init(@size_t int size, @In byte [] data, @size_t int exMsgSize, @Out Pointer exMsg);

    void kenlm_clean(@In Pointer pHandle);

    @Encoding("UTF-8")
    float kenlm_query(@In Pointer pHandle, @In String tag);
}
