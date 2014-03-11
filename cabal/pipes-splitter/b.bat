@echo off
setlocal
call cabal.bat sandbox init
call cabal.bat install
call cabal.bat build
endlocal
