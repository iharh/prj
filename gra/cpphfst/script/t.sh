#! /bin/sh
#export ASAN_SYMBOLIZER_PATH=/usr/bin/llvm-symbolizer
#ASAN_OPTIONS=symbolize=1 build/exe/main/main

valgrind --leak-check=yes build/exe/main/main

#build/exe/main/main
