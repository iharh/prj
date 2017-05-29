package com.clarabridge.clbfoma;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.Encoding;
import jnr.ffi.types.size_t;

public interface LibClbFoma {
    Pointer io_init_text_file(String fileName);
    Pointer io_init_buf_ptr(@size_t int size, byte [] data);

    void io_free(Pointer pIO);

    Pointer iface_load_buf(Pointer pIO);
    void fsm_destroy(Pointer pFsm);

    Pointer apply_init(Pointer pFsm);
    void apply_clear(Pointer pApply);

    @Encoding("UTF-8")
    String apply_up(Pointer pApply, String word);
}
