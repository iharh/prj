#! /usr/bin/env bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
ASAN_OPTIONS=symbolize=1:detect_leaks=1 $CUR_DIR/build/exe/main/debug/smallvec
