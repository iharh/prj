name := "csvstream"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
  "com.netflix.rxjava"      % "rxjava-core"      % "0.14.2",
  "net.sourceforge.javacsv" % "javacsv"          % "2.0",
  //"org.hamcrest"            % "hamcrest-library" % "1.3"     % "test",
  //"org.scalatest"           % "scalatest_2.10"   % "1.9.1"   % "test",
  "com.novocode"            % "junit-interface"  % "0.10"    % "test",
  "junit"                   % "junit"            % "4.11"    % "test"
)
