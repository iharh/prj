@echo off
setlocal
set PATH=build\libs\foma\shared;libs\zlib_win\bin;%PATH%
build\exe\s1\s1.exe
endlocal
