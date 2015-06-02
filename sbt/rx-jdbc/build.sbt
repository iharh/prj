//name := "rx-jdbc"

version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17",

    //"com.oracle"                % "ojdbc6"         % "11.2.0.3",
    //"cn.guoyukun.jdbc"          % "oracle-ojdbc6"  % "11.2.0.3.0",

    "com.typesafe"              % "config"         % "1.3.0",

    "com.github.davidmoten"     % "rxjava-jdbc"    % "0.5.7",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    "com.novocode"              % "junit-interface"    % "0.10"    % "test",
    "junit"                     % "junit"              % "4.11"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

unmanagedJars in Compile ++= {
    //(file("D:/clb/inst/server/lib.cb") ** "*.jar").classpath
    (file("D:/clb/inst/server/lib.cb") ** "ojdbc6*.jar").classpath
}
unmanagedJars in Test ++= {
    //(file("D:/clb/inst/server/lib.cb") ** "*.jar").classpath
    (file("D:/clb/inst/server/lib.cb") ** "ojdbc6*.jar").classpath
}
