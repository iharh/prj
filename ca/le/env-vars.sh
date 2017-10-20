#! /usr/bin/env bash
set -e

ACME_CLIENT_CMD="java -jar lib/acme_client.jar --log-dir . --log-level DEBUG"

ACCOUNT_KEY=~/.ssh/id_rsa

CERT_BASE_DIR=/data/wrk/wnotes/ca
CERT_KEY_DIR=$CERT_BASE_DIR/key
CERT_KEY=$CERT_KEY_DIR/iharh.ml.key

CERT_CNF_DIR=$CERT_BASE_DIR/cnf
CERT_CNF=$CERT_CNF_DIR/iharh.ml.cnf

CERT_CSR_DIR=$CERT_BASE_DIR/csr
CERT_CSR=$CERT_CSR_DIR/iharh.ml.csr

WORK_DIR=$CERT_BASE_DIR/.workdir
WELL_KNOWN_DIR=$CERT_BASE_DIR/.well-known/acme-challenge 

DOMAINS="-d eureka1.iharh.ml -d eureka2.iharh.ml"

CERT_DIR=$CERT_BASE_DIR/cert

CERT_FULLCHAIN=$CERT_DIR/fullchain.pem

EXPORT_DIR=$CERT_BASE_DIR/pkcs12
EXPORT_PKCS12=$EXPORT_DIR/iharh.ml.pkcs12

export PKCS12_PASS=pwd

rm -f acme.log
