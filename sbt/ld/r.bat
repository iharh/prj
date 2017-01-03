@echo off
setlocal
del /q %~dp0*.log
call sbt.bat "run-main ld.LD --- d:/clb/inst/data/ld D:/clb/src/spikes/iharh/ld/twit-based es"
:: java -jar $CUR_DIR/target/ld-assembly-0.1-SNAPSHOT.jar ld.LD /data/wrk/clb/ld /data/wrk/clb/spikes/iharh/ld/selected "$@"
endlocal
