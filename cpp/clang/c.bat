@echo off
setlocal
::x86_64-pc-windows-gnu
::x86_64-w64-mingw32
::-isystem
clang++ -iwithsysroot D:/dev/utils/scoop/apps/gcc/current/lib/gcc/x86_64-w64-mingw32 --target=x86_64-w64-mingw32 main.cpp
endlocal
