@echo off
setlocal
set PATH=bin_tau_pin;%PATH%;
set TAU_CALLPATH=0
::set TAU_CALLPATH=1
::set TAU_CALLPATH_DEPTH=100
pin.exe -t ktaupin.dll -f rules.txt -- testProfileMe.exe
endlocal