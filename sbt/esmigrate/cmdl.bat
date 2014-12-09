@echo off
setlocal
call vars-java-p.bat

set LIB_CB=D:\clb\inst\server\lib.cb

set CP=%LIB_CB%\clarabridge-elasticsearch.jar
set CP=%CP%;%LIB_CB%\clarabridge-classification.jar
set CP=%CP%;%LIB_CB%\commons-cli-1.2.jar
set CP=%CP%;%LIB_CB%\commons-lang-2.6.jar
set CP=%CP%;%LIB_CB%\elasticsearch-1.2.1.jar
set CP=%CP%;%LIB_CB%\lucene-core-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-analyzers-common-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-highlighter-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-join-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-queries-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-queryparser-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-sandbox-4.8.1.jar
set CP=%CP%;%LIB_CB%\lucene-memory-4.8.1.jar
set CP=%CP%;%LIB_CB%\guava-14.0.1.jar
set CP=%CP%;%LIB_CB%\log4j-1.2.17.jar
set CP=%CP%;%LIB_CB%\slf4j-api-1.7.5.jar
set CP=%CP%;%LIB_CB%\slf4j-log4j12-1.7.5.jar
::set CP=%CP%;%LIB_CB%\

java.exe -cp %CP% com.clarabridge.elasticsearch.ingex.migrate.cli.IndexMigratorCLI %*

:: -c epbygomw0024-5432-postgres-win_ss -p 1404 -d _mstokenname,_verbatim,_id_source,_tokendata

endlocal
