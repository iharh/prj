#! /usr/bin/env bash
curl -v -H 'Content-Type:application/octet-stream' -d @./a.txt 'http://localhost:8080/octet'
