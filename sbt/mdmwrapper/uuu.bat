@echo off
setlocal
pushd %~dp0
call run-java.bat -Xmx30m -jar wrapper.jar -r wrapper_mdm.conf
popd
endlocal
