==6937==ERROR: AddressSanitizer: heap-use-after-free on address 0x602000000050 at pc 0x55bab82583ce bp 0x7ffd79f4f610 sp 0x7ffd79f4edc0
READ of size 2 at 0x602000000050 thread T0
    #0 0x55bab82583cd in __interceptor_strlen.part.30 (/data/wrk/prj/gra/cpphfst/build/exe/main/main+0x4e3cd)
    #1 0x55bab8342319 in TransducerAlphabet::~TransducerAlphabet() /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.h:263:30
    #2 0x55bab834272d in TransducerW::~TransducerW() /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.h:685:5
    #3 0x55bab8340d73 in main /data/wrk/prj/gra/cpphfst/src/main/cpp/main.cpp:11:5
    #4 0x7fe71582c4c9 in __libc_start_main (/usr/lib/libc.so.6+0x204c9)
    #5 0x55bab8226099 in _start (/data/wrk/prj/gra/cpphfst/build/exe/main/main+0x1c099)

0x602000000050 is located 0 bytes inside of 2-byte region [0x602000000050,0x602000000052)
freed by thread T0 here:
    #0 0x55bab82deb91 in __interceptor_free.localalias.0 (/data/wrk/prj/gra/cpphfst/build/exe/main/main+0xd4b91)
    #1 0x55bab834251f in TransducerAlphabet::~TransducerAlphabet() /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.h:269:13
    #2 0x55bab8340d4a in main /data/wrk/prj/gra/cpphfst/src/main/cpp/main.cpp:10:23
    #3 0x7fe71582c4c9 in __libc_start_main (/usr/lib/libc.so.6+0x204c9)

previously allocated by thread T0 here:
    #0 0x55bab827c7a9 in strdup (/data/wrk/prj/gra/cpphfst/build/exe/main/main+0x727a9)
    #1 0x55bab831f727 in TransducerAlphabet::get_next_symbol(_IO_FILE*, unsigned short) /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.cc:150:23
    #2 0x55bab8341683 in TransducerAlphabet::TransducerAlphabet(_IO_FILE*, unsigned short) /data/wrk/prj/gra/cpphfst/src/main/cpp/hfst-optimized-lookup.h:249:9
    #3 0x55bab8340cab in main /data/wrk/prj/gra/cpphfst/src/main/cpp/main.cpp:8:24
    #4 0x7fe71582c4c9 in __libc_start_main (/usr/lib/libc.so.6+0x204c9)






==20332== Memcheck, a memory error detector
==20332== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==20332== Using Valgrind-3.13.0 and LibVEX; rerun with -h for copyright info
==20332== Command: build/exe/main/main
==20332== 
==20332== 
==20332== HEAP SUMMARY:
==20332==     in use at exit: 28,062 bytes in 328 blocks
==20332==   total heap usage: 437,105 allocs, 436,777 frees, 16,628,749 bytes allocated
==20332== 
==20332== 19 bytes in 1 blocks are definitely lost in loss record 1 of 12
==20332==    at 0x4C2BE7F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==20332==    by 0x576B5EA: strdup (in /usr/lib/libc-2.25.so)
==20332==    by 0x10AADF: TransducerAlphabet::get_next_symbol(_IO_FILE*, unsigned short) (hfst-optimized-lookup.cc:156)
==20332==    by 0x10FAEC: TransducerAlphabet::TransducerAlphabet(_IO_FILE*, unsigned short) (hfst-optimized-lookup.h:247)
==20332==    by 0x12A15E: main (main.cpp:8)
==20332== 
==20332== 104 bytes in 1 blocks are definitely lost in loss record 3 of 12
==20332==    at 0x4C2CCBF: operator new[](unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==20332==    by 0x10A45C: TransducerHeader::skip_hfst3_header(_IO_FILE*) (hfst-optimized-lookup.cc:68)
==20332==    by 0x10F4CE: TransducerHeader::TransducerHeader(_IO_FILE*) (hfst-optimized-lookup.h:130)
==20332==    by 0x12A133: main (main.cpp:7)
==20332== 
==20332== 2,000 bytes in 1 blocks are definitely lost in loss record 6 of 12
==20332==    at 0x4C2BE7F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==20332==    by 0x1113B5: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:1006)
==20332==    by 0x12A1F0: main (main.cpp:10)
==20332== 
==20332== 7,753 (48 direct, 7,705 indirect) bytes in 1 blocks are definitely lost in loss record 10 of 12
==20332==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==20332==    by 0x10FA1C: TransducerAlphabet::TransducerAlphabet(_IO_FILE*, unsigned short) (hfst-optimized-lookup.h:238)
==20332==    by 0x12A15E: main (main.cpp:8)
==20332== 
==20332== 18,186 (240 direct, 17,946 indirect) bytes in 5 blocks are definitely lost in loss record 12 of 12
==20332==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==20332==    by 0x10ABAF: LetterTrie::add_string(char const*, unsigned short) (hfst-optimized-lookup.cc:170)
==20332==    by 0x10ADC6: Encoder::read_input_symbols(std::map<unsigned short, char const*, std::less<unsigned short>, std::allocator<std::pair<unsigned short const, char const*> > >*) (hfst-optimized-lookup.cc:204)
==20332==    by 0x10FDA8: Encoder::Encoder(std::map<unsigned short, char const*, std::less<unsigned short>, std::allocator<std::pair<unsigned short const, char const*> > >*, unsigned short) (hfst-optimized-lookup.h:300)
==20332==    by 0x11134F: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:1009)
==20332==    by 0x12A1F0: main (main.cpp:10)
==20332== 
==20332== LEAK SUMMARY:
==20332==    definitely lost: 2,411 bytes in 9 blocks
==20332==    indirectly lost: 25,651 bytes in 319 blocks
==20332==      possibly lost: 0 bytes in 0 blocks
==20332==    still reachable: 0 bytes in 0 blocks
==20332==         suppressed: 0 bytes in 0 blocks
==20332== 
==20332== For counts of detected and suppressed errors, rerun with: -v
==20332== ERROR SUMMARY: 5 errors from 5 contexts (suppressed: 0 from 0)
