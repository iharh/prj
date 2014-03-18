@echo off
setlocal
call cabal.bat sandbox init
call cabal.bat install --enable-benchmarks
::call cabal.bat bench
endlocal
