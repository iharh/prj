#! /usr/bin/env bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

keytool -list -v -keystore $EXPORT_PKCS12 -storepass $PKCS12_PASS
