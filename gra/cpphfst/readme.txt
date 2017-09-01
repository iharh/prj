==30628==ERROR: AddressSanitizer: global-buffer-overflow on address 0x0038f10a3b61 at pc 0x0038f1026668 bp 0x7ffef493b400 sp 0x7ffef493b3f8
READ of size 1 at 0x0038f10a3b61 thread T0
    #0 0x38f1026667 in LetterTrie::add_string(char const*, unsigned short) /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.cc:163:7
    #1 0x38f1027123 in Encoder::read_input_symbols(std::map<unsigned short, char const*, std::less<unsigned short>, std::allocator<std::pair<unsigned short const, char const*> > >*) /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.cc:204:15
    #2 0x38f1051c7d in Encoder::Encoder(std::map<unsigned short, char const*, std::less<unsigned short>, std::allocator<std::pair<unsigned short const, char const*> > >*, unsigned short) /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.h:300:5
    #3 0x38f10396c3 in TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.h:980:5
    #4 0x38f108cda1 in main /data/wrk/prj/gra/cpphfst/src/main/cpp/main.cpp:10:27
    #5 0x7efd069ff4c9 in __libc_start_main (/usr/lib/libc.so.6+0x204c9)
    #6 0x38f0f2cdc9 in _start (/data/wrk/prj/gra/cpphfst/build/exe/main/main+0x1cdc9)

0x0038f10a3b61 is located 63 bytes to the left of global variable '<string literal>' defined in '/data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.cc:291:20' (0x38f10a3ba0) of size 63
  '<string literal>' is ascii string '!! Warning: transducer has epsilon cycles                  !!
'
0x0038f10a3b61 is located 0 bytes to the right of global variable '<string literal>' defined in '/data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.cc:141:34' (0x38f10a3b60) of size 1
  '<string literal>' is ascii string ''
SUMMARY: AddressSanitizer: global-buffer-overflow /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.cc:163:7 in LetterTrie::add_string(char const*, unsigned short)
Shadow bytes around the buggy address:
  0x00079e20c710: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x00079e20c720: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x00079e20c730: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  0x00079e20c740: 05 f9 f9 f9 f9 f9 f9 f9 05 f9 f9 f9 f9 f9 f9 f9
  0x00079e20c750: 00 f9 f9 f9 f9 f9 f9 f9 00 01 f9 f9 f9 f9 f9 f9
=>0x00079e20c760: 00 00 00 00 00 00 03 f9 f9 f9 f9 f9[01]f9 f9 f9
  0x00079e20c770: f9 f9 f9 f9 00 00 00 00 00 00 00 07 f9 f9 f9 f9
  0x00079e20c780: 00 00 00 00 00 00 00 07 f9 f9 f9 f9 00 00 00 00
  0x00079e20c790: 00 00 00 07 f9 f9 f9 f9 04 f9 f9 f9 f9 f9 f9 f9
  0x00079e20c7a0: 02 f9 f9 f9 f9 f9 f9 f9 00 05 f9 f9 f9 f9 f9 f9
  0x00079e20c7b0: 00 00 01 f9 f9 f9 f9 f9 00 07 f9 f9 f9 f9 f9 f9
Shadow byte legend (one shadow byte represents 8 application bytes):
  Addressable:           00
  Partially addressable: 01 02 03 04 05 06 07 
  Heap left redzone:       fa
  Freed heap region:       fd
  Stack left redzone:      f1
  Stack mid redzone:       f2
  Stack right redzone:     f3
  Stack after return:      f5
  Stack use after scope:   f8
  Global redzone:          f9
  Global init order:       f6
  Poisoned by user:        f7
  Container overflow:      fc
  Array cookie:            ac
  Intra object redzone:    bb
  ASan internal:           fe
  Left alloca redzone:     ca
  Right alloca redzone:    cb
==30628==ABORTING
