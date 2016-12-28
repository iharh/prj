import org.apache.commons.lang3.SystemUtils
version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

//testOptions += Seq("-encoding", "UTF-8")
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.22",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.22",
    "log4j"                     % "log4j"          % "1.2.17",

    "commons-pool"              % "commons-pool"   % "1.6",
    "com.ibm.icu"               % "icu4j"          % "58.2", // 4.6 4.8.1.1
    "org.apache.commons"        % "commons-csv"    % "1.4",

    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

val libroot = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/clb" else
    "D:/clb/inst/server"

val libcb = libroot + "/lib.cb"

unmanagedJars in Compile ++= {
    (file(libcb) ** "*.jar").classpath
}

//mainClass in (Compile, run, assembly) := Some("ld.LD")
mainClass in assembly := Some("ld.LD")

// assembly
