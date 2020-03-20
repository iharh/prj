#! /usr/bin/env bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
#export LD_LIBRARY_PATH=build/libs/smallrt/shared:$LD_LIBRARY_PATH
$CUR_DIR/build/exe/main/debug/simple
