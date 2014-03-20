@echo off
setlocal
call cabal.bat sandbox init
call cabal.bat install --enable-tests --enable-benchmarks --flags=documentation --haddock-hyperlink-source --only-dependencies
call cabal.bat build
call cabal.bat haddock --hyperlink-source
call cabal.bat test
call cabal.bat bench
:: call cabal.bat repl
endlocal
