@echo off
setlocal
call vars-vc10.bat
:: /?
dumpbin /exports build\libs\clbfoma\shared\clbfoma.dll
endlocal
