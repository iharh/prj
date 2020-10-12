#! /usr/bin/env bash

# /bin/sh ../libtool --preserve-dup-deps   --tag=CC   --mode=compile gcc -DHAVE_CONFIG_H -I. -I/data/wrk/clb/fx/spikes/gra/third_party/scws/build/scws-1.2.3/libscws -I..     -g -O2 -MT charset.lo -MD -MP -MF .deps/charset.Tpo -c -o charset.lo /data/wrk/clb/fx/spikes/gra/third_party/scws/build/scws-1.2.3/libscws/charset.c


rm -rf build
mkdir build
mkdir build/obj
mkdir build/static
mkdir build/cli

#-Wall -Wextra
gcc -c src/libmy.c -o build/obj/libmy.o
g++ -c src/main.cpp -o build/obj/main.o
# rcs
ar -cvq build/static/libmy.a build/obj/libmy.o
g++ build/obj/main.o -Lbuild/static -lmy -o build/cli/main
