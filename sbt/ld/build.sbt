version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

//testOptions += Seq("-encoding", "UTF-8")
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.21",
    "log4j"                     % "log4j"          % "1.2.17",

    "commons-pool"              % "commons-pool"   % "1.6",
    "com.ibm.icu"               % "icu4j"          % "4.6",
    "org.apache.commons"        % "commons-csv"    % "1.2",

    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

unmanagedJars in Compile ++= {
    (file("/data/wrk/clb/lib.cb") ** "*.jar").classpath
}

//mainClass in (Compile, run, assembly) := Some("ld.LD")
mainClass in assembly := Some("ld.LD")

// assembly
// dependencyUpdates
