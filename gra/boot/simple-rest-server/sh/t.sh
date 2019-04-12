#!/usr/bin/env bash
set -o errexit
DATA_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../data" && pwd )"
curl -XPOST http://localhost:8080/bench -H "Content-Type: application/json" -d {\"id\":\"id2\",\"iter\":\"2\",\"file\":\"$DATA_DIR/a.txt\"\}
