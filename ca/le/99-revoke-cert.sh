#! /usr/bin/env bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

$ACME_CLIENT_CMD --command revoke-certificate -a $ACCOUNT_KEY -w $WORK_DIR
