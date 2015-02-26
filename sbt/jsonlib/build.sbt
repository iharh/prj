//name := "jsonlib"

//version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint", "-deprecation", "-unchecked", "-Xfatal-warnings")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

parallelExecution in Test := false

//logLevel := Level.Debug

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6",
    //"commons-cli"               % "commons-cli"    % "1.2",
    "net.sf.json-lib"           % "json-lib"       % "2.2.2" classifier "jdk15",
    // testing stuff
    "com.novocode"              % "junit-interface"    % "0.10"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    //"org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
    "org.hamcrest"              % "java-hamcrest"   % "2.0.0.0"     % "test"
)

//unmanagedJars in Compile ++= {
    //(file("D:/.../lib") ** "*.jar").classpath

    //val base = baseDirectory.value
    //val baseDirectories = (base / "libA") +++ (base / "b" / "lib") +++ (base / "libC")
    //val customJars = (baseDirectories ** "*.jar") +++ (base / "d" / "my.jar")
    //customJars.classpath
//}
