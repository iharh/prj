#! /usr/bin/env bash
CUR_ITER=`cat n.txt | cut -c18- | sort -n -r | head -n 1`
echo CUR_ITER: $CUR_ITER
