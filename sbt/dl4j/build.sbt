version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"          % "1.7.12",
    "org.slf4j"                 % "slf4j-log4j12"      % "1.7.12",
    "log4j"                     % "log4j"              % "1.2.17",

    "io.reactivex"              % "rxjava-string"      % "0.22.0",
    
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)
//http://stackoverflow.com/questions/15560598/play-2-0-sbt-exclude-certain-transitive-dependencies-from-some-all-modules-in

//libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "0.0.3.3.4.alpha2" excludeAll(
//libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "0.4-rc1.2" excludeAll(

//libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "0.0.3.3.4.alpha2" excludeAll(
libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "0.4-rc1.2" excludeAll(
        ExclusionRule(organization = "io.dropwizard"),
        ExclusionRule(organization = "ch.qos.logback"),
        ExclusionRule(organization = "com.typesafe.akka")
)

//libraryDependencies += "org.nd4j"           % "nd4j-jblas" % "0.4-rc1.2" excludeAll(
//libraryDependencies += "org.nd4j"           % "nd4j-jblas" % "0.0.3.5.5.5" excludeAll(

//libraryDependencies += "org.nd4j"           % "nd4j-netlib-blas" % "0.4-rc1.2" excludeAll(
//libraryDependencies += "org.nd4j"           % "nd4j-netlib-blas" % "0.0.3.5.5.5" excludeAll(

// nd4j-jblas
// nd4j-netlib-blas
// nd4j-x86
//libraryDependencies += "org.nd4j"           % "nd4j-netlib-blas" % "0.0.3.5.5.5" excludeAll(
libraryDependencies += "org.nd4j"           % "nd4j-netlib-blas" % "0.4-rc1.2" excludeAll(
        ExclusionRule(organization = "io.dropwizard"),
        ExclusionRule(organization = "ch.qos.logback"),
        ExclusionRule(organization = "com.typesafe.akka")
)

// sbt one-jar
seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

mainClass in (Compile, run) := Some("dl4j.Dl4jRun")
