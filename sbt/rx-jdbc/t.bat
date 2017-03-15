@echo off
setlocal
del /Q %~dp0*.log
del /Q %~dp0out\*.xml
::call sbt.bat test
set EXTRA_OPTS="-Djava.library.path=D:/clb/inst/fx"
:: pg/epbygomw0024
:: pg/qa-mohamed
:: "-DschemeFile=in/en-new1.xml"
:: "-DschemeFile=d:/clb/src/main/lang-packs/english/installer/target/Content/fx/en/scheme/english.scheme"
call sbt.bat "-Ddbcfg=ora/oracle-qa" "testOnly *PropTest"
::call sbt.bat "-Ddbcfg=ora/oracle-qa" "testOnly *ProjTest"
::call sbt.bat %EXTRA_OPTS% "-DschemeFile=in/en-local.xml" "testOnly *SchemeDumpTest"
::call sbt.bat "-Ddbcfg=pg/epbygomw0024" "testOnly *MappingTest"
::call sbt.bat "-Ddbcfg=pg/qa-mohamed" "testOnly *FileRepoListTest"
::SENT.EXPORT.TARGET_DIR
::call sbt.bat "testOnly *ComponentTest"
::call sbt.bat "testOnly *ConnectTest"
::call sbt.bat "testOnly *BaseDecodeTest"
::call sbt.bat "testOnly *ConfTest"
::call sbt.bat "testOnly *ProjTest"
::call sbt.bat "testOnly *RuleItemTest"
::call sbt.bat "testOnly *CBCMPTypeTest"
::call sbt.bat "testOnly *ProjCountTest"
::call sbt.bat "testOnly *CleanCountTest"
::call sbt.bat "testOnly *CleanExportTest"
::call sbt.bat "testOnly *StagingTest"
endlocal
