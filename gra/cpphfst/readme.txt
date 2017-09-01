==13838== Memcheck, a memory error detector
==13838== Copyright (C) 2002-2017, and GNU GPL'd, by Julian Seward et al.
==13838== Using Valgrind-3.13.0 and LibVEX; rerun with -h for copyright info
==13838== Command: build/exe/main/main
==13838== 
==13838== 
==13838== HEAP SUMMARY:
==13838==     in use at exit: 8,147,328 bytes in 437,035 blocks
==13838==   total heap usage: 437,105 allocs, 70 frees, 16,628,749 bytes allocated
==13838== 
==13838== 12 bytes in 1 blocks are definitely lost in loss record 1 of 18
==13838==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10D914: TransitionTableReaderW::get_transition_vector() (hfst-optimized-lookup.cc:997)
==13838==    by 0x110EF8: TransitionTableReaderW::TransitionTableReaderW(_IO_FILE*, unsigned int) (hfst-optimized-lookup.h:860)
==13838==    by 0x1111C5: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 12 bytes in 1 blocks are definitely lost in loss record 2 of 18
==13838==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10D944: TransitionTableReaderW::get_transition_vector() (hfst-optimized-lookup.cc:998)
==13838==    by 0x110EF8: TransitionTableReaderW::TransitionTableReaderW(_IO_FILE*, unsigned int) (hfst-optimized-lookup.h:860)
==13838==    by 0x1111C5: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 19 bytes in 1 blocks are definitely lost in loss record 3 of 18
==13838==    at 0x4C2BE7F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x576B5EA: strdup (in /usr/lib/libc-2.25.so)
==13838==    by 0x10AADF: TransducerAlphabet::get_next_symbol(_IO_FILE*, unsigned short) (hfst-optimized-lookup.cc:156)
==13838==    by 0x10FAEC: TransducerAlphabet::TransducerAlphabet(_IO_FILE*, unsigned short) (hfst-optimized-lookup.h:247)
==13838==    by 0x129F2E: main (main.cpp:8)
==13838== 
==13838== 104 bytes in 1 blocks are definitely lost in loss record 5 of 18
==13838==    at 0x4C2CCBF: operator new[](unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10A45C: TransducerHeader::skip_hfst3_header(_IO_FILE*) (hfst-optimized-lookup.cc:68)
==13838==    by 0x10F4CE: TransducerHeader::TransducerHeader(_IO_FILE*) (hfst-optimized-lookup.h:130)
==13838==    by 0x129F03: main (main.cpp:7)
==13838== 
==13838== 2,000 bytes in 1 blocks are definitely lost in loss record 8 of 18
==13838==    at 0x4C2BE7F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x11125F: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:982)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 7,753 (48 direct, 7,705 indirect) bytes in 1 blocks are definitely lost in loss record 12 of 18
==13838==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10FA1C: TransducerAlphabet::TransducerAlphabet(_IO_FILE*, unsigned short) (hfst-optimized-lookup.h:238)
==13838==    by 0x129F2E: main (main.cpp:8)
==13838== 
==13838== 18,186 (240 direct, 17,946 indirect) bytes in 5 blocks are definitely lost in loss record 14 of 18
==13838==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10ABAF: LetterTrie::add_string(char const*, unsigned short) (hfst-optimized-lookup.cc:170)
==13838==    by 0x10ADC6: Encoder::read_input_symbols(std::map<unsigned short, char const*, std::less<unsigned short>, std::allocator<std::pair<unsigned short const, char const*> > >*) (hfst-optimized-lookup.cc:204)
==13838==    by 0x10FDA8: Encoder::Encoder(std::map<unsigned short, char const*, std::less<unsigned short>, std::allocator<std::pair<unsigned short const, char const*> > >*, unsigned short) (hfst-optimized-lookup.h:300)
==13838==    by 0x1111F9: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 1,416,978 bytes in 1 blocks are definitely lost in loss record 15 of 18
==13838==    at 0x4C2BE7F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x110DD9: IndexTableReaderW::IndexTableReaderW(_IO_FILE*, unsigned int) (hfst-optimized-lookup.h:811)
==13838==    by 0x111199: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 1,889,304 bytes in 236,163 blocks are definitely lost in loss record 16 of 18
==13838==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10D76F: IndexTableReaderW::get_index_vector() (hfst-optimized-lookup.cc:963)
==13838==    by 0x110E18: IndexTableReaderW::IndexTableReaderW(_IO_FILE*, unsigned int) (hfst-optimized-lookup.h:819)
==13838==    by 0x111199: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 2,406,480 bytes in 1 blocks are definitely lost in loss record 17 of 18
==13838==    at 0x4C2BE7F: malloc (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x110EB9: TransitionTableReaderW::TransitionTableReaderW(_IO_FILE*, unsigned int) (hfst-optimized-lookup.h:856)
==13838==    by 0x1111C5: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== 2,406,480 bytes in 200,540 blocks are definitely lost in loss record 18 of 18
==13838==    at 0x4C2C54F: operator new(unsigned long) (in /usr/lib/valgrind/vgpreload_memcheck-amd64-linux.so)
==13838==    by 0x10D8B0: TransitionTableReaderW::get_transition_vector() (hfst-optimized-lookup.cc:994)
==13838==    by 0x110EF8: TransitionTableReaderW::TransitionTableReaderW(_IO_FILE*, unsigned int) (hfst-optimized-lookup.h:860)
==13838==    by 0x1111C5: TransducerW::TransducerW(_IO_FILE*, TransducerHeader, TransducerAlphabet) (hfst-optimized-lookup.h:985)
==13838==    by 0x129FC0: main (main.cpp:10)
==13838== 
==13838== LEAK SUMMARY:
==13838==    definitely lost: 8,121,677 bytes in 436,716 blocks
==13838==    indirectly lost: 25,651 bytes in 319 blocks
==13838==      possibly lost: 0 bytes in 0 blocks
==13838==    still reachable: 0 bytes in 0 blocks
==13838==         suppressed: 0 bytes in 0 blocks
==13838== 
==13838== For counts of detected and suppressed errors, rerun with: -v
==13838== ERROR SUMMARY: 11 errors from 11 contexts (suppressed: 0 from 0)
