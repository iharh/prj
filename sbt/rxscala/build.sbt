import org.apache.commons.lang3.SystemUtils

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

//autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
//testOptions in Test += ...
// > test-only org.acme.RedSuite -- -oD

libraryDependencies ++= Seq(
    "com.nrinaudo"             %% "kantan.csv"     % "0.1.11",

    "org.slf4j"                 % "slf4j-api"      % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.21",
    "log4j"                     % "log4j"          % "1.2.17",

    "commons-io"                % "commons-io"     % "2.5",
    "org.apache.commons"        % "commons-lang3"  % "3.4",
    //"com.oracle"                % "ojdbc6"         % "11.2.0.3",
    //"cn.guoyukun.jdbc"          % "oracle-ojdbc6"  % "11.2.0.3.0",

    "com.typesafe"              % "config"         % "1.3.0",

    "com.github.davidmoten"     % "rxjava-jdbc"    % "0.7.1",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6",
    //
    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test",

    "org.scalatest"             % "scalatest_2.11"     % "2.2.6"   % "test"
    //"org.scalacheck"              %% "scalacheck"      % "1.12.5"
)

val libcb = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/clb/lib.cb" else
    "D:/clb/inst/server/lib.cb"

unmanagedJars in Compile ++= {
    (file(libcb) ** "ojdbc*.jar").classpath // ojdbc6* ojdbc7*
}

//unmanagedJars in Compile ++= {
//    (file("D:/clb/inst/server/lib.cb") ** "postgresql-9*.jar").classpath
//}

//unmanagedJars in Compile ++= {
//    (file("D:/clb/inst/server/lib.cb") ** "elasticsearch-*.jar").classpath
//}

//unmanagedJars in Compile ++= {
//    (file("D:/clb/inst/server/lib.cb") ** "protobuf-java-*.jar").classpath
//}
