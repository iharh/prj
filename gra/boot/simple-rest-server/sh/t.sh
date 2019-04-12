#!/usr/bin/env bash
set -o errexit
curl -XPOST http://localhost:8080/bench -H "Content-Type: application/json" -d '{ "id" : "id1", "iter" : "10" }'
