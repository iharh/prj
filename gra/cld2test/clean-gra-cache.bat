@echo off
setlocal
call vars-gradle.bat
set MOD_DIR=%GRADLE_USER_HOME%\caches\modules-2
set MOD_ID=clarabridge\cld2-windows

rd /s /q %MOD_DIR%\files-2.1\%MOD_ID%
rd /s /q %MOD_DIR%\metadata-2.23\descriptors\%MOD_ID%
endlocal
