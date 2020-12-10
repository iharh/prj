#! /usr/bin/env bash
CUR_ITER=`cat n.txt | cut -c18- | sort -n -r | head -n 1`
echo CUR_ITER: $CUR_ITER. Enter var: 
#local VAR
read -r VAR
echo VAR: [$VAR]
if [[ $VAR == "y" ]]; then
    echo "do gc"
else
    echo "skip gc"
fi
