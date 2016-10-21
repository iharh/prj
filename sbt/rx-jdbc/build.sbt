import org.apache.commons.lang3.SystemUtils
//name := "rx-jdbc"

version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.21",
    "log4j"                     % "log4j"          % "1.2.17",

    "commons-io"                % "commons-io"     % "2.5",
    //"com.oracle"                % "ojdbc6"         % "11.2.0.3",
    //"cn.guoyukun.jdbc"          % "oracle-ojdbc6"  % "11.2.0.3.0",

    "com.typesafe"              % "config"         % "1.3.1",

    "com.github.davidmoten"     % "rxjava-jdbc"    % "0.7.2",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

val libroot = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/clb" else
    "D:/clb/inst/server"

val libcb = libroot + "/lib.cb"

val libfx = libroot + "/lib.fx"

unmanagedJars in Compile ++= {
    (file(libcb) ** "ojdbc*.jar").classpath // ojdbc6* ojdbc7*
}

unmanagedJars in Compile ++= {
    (file(libcb) ** "postgresql-9*.jar").classpath
}

unmanagedJars in Compile ++= {
    (file(libcb) ** "elasticsearch-*.jar").classpath
}

unmanagedJars in Compile ++= {
    (file(libcb) ** "protobuf-java-*.jar").classpath
}

unmanagedJars in Compile ++= {
    (file(libcb) ** "protobuf-java-*.jar").classpath
}

unmanagedJars in Compile ++= {
    (file(libfx) ** "*.jar").classpath
}

// javaOptions in test_or_run += "-Djava.library.path=D:/clb/inst/fx"
