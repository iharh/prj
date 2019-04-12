#!/usr/bin/env bash
set -o errexit
curl -XPOST http://localhost:8080/bench -H "Content-Type: application/json" -d '{ "id" : "id2", "iter" : "10" }'
