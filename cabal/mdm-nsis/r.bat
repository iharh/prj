@echo off
setlocal
::%~dp0.cabal-sandbox\bin\mdm-nsis.exe
%~dp0dist\build\mdm-nsis\mdm-nsis.exe
:: cabal run
endlocal
