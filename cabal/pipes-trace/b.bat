@echo off
setlocal
call cabal.bat sandbox init
call cabal.bat configure
call cabal.bat install --only-dependencies
call cabal.bat build
endlocal
