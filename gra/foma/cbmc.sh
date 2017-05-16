#! /bin/sh
cbmc --no-unwinding-assertions --unwind 5 src/main/c/s1/main.c
