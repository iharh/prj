#! /usr/bin/env bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

openssl req -outform PEM -out $CERT_CSR -config $CERT_CNF -new -key $CERT_KEY -nodes -subj "/"
