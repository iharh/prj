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
