//version := "1.0-SNAPSHOT"

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
  "io.netty"                % "netty-all"             % "5.0.0.Alpha1",
  //"org.scalatest"           % "scalatest_2.10"   % "1.9.1"   % "test",
  "com.novocode"            % "junit-interface"       % "0.10"   % "test",
  "junit"                   % "junit"                 % "4.11"   % "test"
)
