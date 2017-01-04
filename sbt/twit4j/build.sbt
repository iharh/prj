import org.apache.commons.lang3.SystemUtils

scalaVersion := "2.12.1"

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.22",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.22",
    "log4j"                     % "log4j"          % "1.2.17",

    "commons-pool"              % "commons-pool"   % "1.6",
    "com.ibm.icu"               % "icu4j"          % "58.2", // 4.6 4.8.1.1
    "org.apache.commons"        % "commons-csv"    % "1.4",

    "com.typesafe"              % "config"         % "1.3.1",
    "org.twitter4j"             % "twitter4j-core" % "4.0.4",

    "com.nrinaudo"             %% "kantan.csv"     % "0.1.16",
    // TODO: try only monix-reactive
    "io.monix"                 %% "monix"          % "2.1.2",
    //
    "org.scalatest"            %% "scalatest"      % "3.0.1"   % "test"
)

val libroot = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/clb" else
    "D:/clb/inst/server"

val libcb = libroot + "/lib.cb"

unmanagedJars in Compile ++= {
    (file(libcb) ** "*.jar").classpath
}

val cfgtwitter = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/notes/wrk/clb/hosts/twitter" else
    "D:/dev/notes/wrk/clb/hosts/twitter"

unmanagedClasspath in Test += file(cfgtwitter)
