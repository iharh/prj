#! /usr/bin/env bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

rm -f $WORK_DIR/*
rm -f $WELL_KNOWN_DIR/*
$ACME_CLIENT_CMD --command authorize-domains -a $ACCOUNT_KEY -w $WORK_DIR --well-known-dir $WELL_KNOWN_DIR --one-dir-for-well-known $DOMAINS
