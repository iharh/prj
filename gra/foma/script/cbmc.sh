#! /bin/sh
cbmc --no-unwinding-assertions --unwind 2 src/main/c/s1/main.c
