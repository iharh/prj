#!/bin/bash
# ??? how to utilize an alias?
stack --stack-root=/data/wrk/.stack build
stack --stack-root=/data/wrk/.stack exec d1-exe -- -o d1.svg -w 400
