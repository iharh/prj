version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"          % "1.7.12",
    "org.slf4j"                 % "slf4j-log4j12"      % "1.7.12",
    "log4j"                     % "log4j"              % "1.2.17",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

//http://stackoverflow.com/questions/15560598/play-2-0-sbt-exclude-certain-transitive-dependencies-from-some-all-modules-in
libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "0.0.3.3.4.alpha2" excludeAll(
        ExclusionRule(organization = "io.dropwizard"),
        ExclusionRule(organization = "ch.qos.logback")
)
libraryDependencies += "org.nd4j"           % "nd4j-jblas" % "0.0.3.5.5.5" excludeAll(
        ExclusionRule(organization = "io.dropwizard"),
        ExclusionRule(organization = "ch.qos.logback")
)
