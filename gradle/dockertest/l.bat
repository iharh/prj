@echo off
setlocal
call vars-d2m-cmd.bat

call gradle.bat -q listImages %*
::-DdockerServerUrl=https://192.168.99.100:2376 -DdockerCertPath=F:/vbox-dm/certs
endlocal
