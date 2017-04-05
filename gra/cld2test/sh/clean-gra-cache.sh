#!/usr/bin/env bash
GRADLE_USER_HOME=~/.gradle
MOD_DIR=$GRADLE_USER_HOME/caches/modules-2
MOD_ID=clarabridge/libcld2-linux

rm -rf $MOD_DIR/files-2.1/$MOD_ID
rm -rf $MOD_DIR/metadata-2.23/descriptors/$MOD_ID
