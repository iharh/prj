//name := "rx-jdbc"

// version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

// one-jar stuff
seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17",

    "com.github.davidmoten"     % "rxjava-jdbc"    % "0.5.7",

    "org.antlr"                 % "ST4"            % "4.0.8",

    "commons-io"                % "commons-io"     % "2.4",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    "com.novocode"              % "junit-interface"    % "0.10"    % "test",
    "junit"                     % "junit"              % "4.11"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

val clbIvyCache = file("D:/clb/src/platform/ivy-cache")

unmanagedJars in Compile ++= {
    ((clbIvyCache / "com.oracle/ojdbc6/jars") ** "ojdbc6*.jar").classpath
}

unmanagedJars in Compile ++= {
    ((clbIvyCache / "postgresql/postgresql/jars") ** "postgresql-9*.jar").classpath
}

mainClass in (Compile, run) := Some("rxjdbc.PSUpdater")
