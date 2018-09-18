@echo off
setlocal
set PATH=bin_tau_pin;%PATH%;
pprof.exe > profile.txt
endlocal