#! /usr/bin/env bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

java -jar acme_client.jar $OPT_LOG --command authorize-domains -a $ACCOUNT_KEY -w $WORK_DIR --well-known-dir $WELL_KNOWN_DIR --one-dir-for-well-known $DOMAINS