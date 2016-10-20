import org.apache.commons.lang3.SystemUtils

//version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

//autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

//testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
//testOptions in Test += ...
// > test-only org.acme.RedSuite -- -oD

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"       % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"   % "1.7.21",
    "log4j"                     % "log4j"           % "1.2.17",

    "org.apache.commons"        % "commons-lang3"   % "3.5",

    "com.typesafe"              % "config"          % "1.3.1",

    //"io.getquill"               %% "quillc"        % "0.8.0",
    "io.getquill"               %% "quill-jdbc"     % "0.10.0",
    //https://jdbc.postgresql.org/download.html
    //https://mvnrepository.com/artifact/postgresql/postgresql
    //https://mvnrepository.com/artifact/org.postgresql/postgresql
    //"org.postgresql"            % "postgresql"      % "9.4.1209",
    //https://www.versioneye.com/java/postgresql:postgresql/9.1-902.jdbc4
    //"postgresql"                % "postgresql"      % "9.1-902.jdbc4",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6",
    //
    //"org.scalatest"             % "scalatest_2.11"     % "3.0.0"   % "test"
    "org.scalatest"             %% "scalatest"      % "3.0.0"   % "test"
)

val libcb = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/clb/lib.cb" else
    "D:/clb/inst/server/lib.cb"

unmanagedJars in Compile ++= {
    (file(libcb) ** "postgresql-9*.jar").classpath // postgresql-9.1-902.jdbc4.jar
}
//    (file(libcb) ** "ojdbc*.jar").classpath // ojdbc6* ojdbc7*
//    (file("D:/clb/inst/server/lib.cb") ** "protobuf-java-*.jar").classpath
