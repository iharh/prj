@echo off
setlocal
call cabal.bat sandbox init
call cabal.bat install --enable-tests
::call cabal.bat test
endlocal
