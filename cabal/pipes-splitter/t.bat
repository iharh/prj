@echo off
setlocal
%~dp0.cabal-sandbox\bin\pipes-splitter.exe < a.txt
endlocal
