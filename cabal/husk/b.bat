@echo off
setlocal
call cabal.bat sandbox init
:: cabal configure
call cabal.bat install --only-dependencies
call cabal.bat build
:: cabal repl
:: cabal haddock --hyperlink-source
endlocal
