@echo off
setlocal
::%~dp0.cabal-sandbox\bin\pipes-trace.exe
%~dp0dist\build\pipes-trace\pipes-trace.exe
:: cabal run
endlocal
