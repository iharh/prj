#! /usr/bin/env bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

#openssl pkcs12 -help
#-chain

rm -f $EXPORT_PKCS12
openssl pkcs12 -export -password "env:PKCS12_PASS" -out $EXPORT_PKCS12 -inkey $CERT_KEY -in $CERT_FULLCHAIN
