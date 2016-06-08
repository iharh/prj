@echo off
curl -H "Content-Type: application/json" -XPOST http://localhost:18080/cbtests/rest/tests/run -d @s.json
