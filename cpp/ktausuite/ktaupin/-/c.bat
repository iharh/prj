@echo off
setlocal

call "%VS90COMNTOOLS%..\..\VC\vcvarsall.bat" x86

nmake /f Makefile.win32

endlocal
