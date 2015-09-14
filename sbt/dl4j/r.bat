@echo off
setlocal
call vars-java8.bat
::call vars-java7.bat

::set JIT_OPTS=-XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading -XX:+LogCompilation 
set JIT_OPTS=-XX:+UnlockDiagnosticVMOptions -XX:+TraceClassLoading -XX:-TieredCompilation -XX:+PrintCompilation -XX:+PrintInlining
set JAVA_OPTS=-Xms12G -Xmx12G -Xss1M -XX:+CMSClassUnloadingEnabled -XX:ReservedCodeCacheSize=128m -Dsbt.log.format=true -Dfile.encoding=UTF-8
::%JIT_OPTS%
::-Xms12G -Xmx12G

%JAVA_HOME%/bin/java %JAVA_OPTS% -jar "%~dp0target\dl4j-0.1-SNAPSHOT-one-jar.jar" %* > out.txt 2>&1

:: sbt "run-main dl4j.Dl4jRun"

endlocal
