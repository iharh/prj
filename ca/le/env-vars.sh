#! /usr/bin/env bash
set -e

OPT_LOG="--log-dir . --log-level DEBUG"

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

rm -f acme.log
