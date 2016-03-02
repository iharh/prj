version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.18",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.18",
    "log4j"                     % "log4j"          % "1.2.17",

    "io.reactivex"              % "rxjava"            % "1.1.1",
    //"io.reactivex"              % "rxjava-async-util" % "0.21.0",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)
