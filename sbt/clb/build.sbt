import Rainbow._

parallelExecution := false

//traceLevel := -1

lazy val hud = taskKey[Unit]("hud echo task")
lazy val lll = taskKey[Unit]("lll echo task")
lazy val eee = taskKey[Unit]("eee echo task")

def echo_x(n: String): Unit = {
    println { "Warning - some text!".red }
//    println("Warning " + RED + "some" + RESET + " text!")
    println("echoing: " + n)
}

hud := echo_x("hud")
lll := echo_x("lll")

eee := {
    //val log = streams.value.log
    //log.error("abc defg")

    // check the Pre.scala
    //assert (true == false, "assertion1")
    //require (false, "defg")

    //sys.error(...)
    //error("my error")

    println("some out")
}

//addCommandAlias("copyall", ";copydf;copynotes;copyprj")

//import scala.Console._

//scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-language:implicitConversions")
//... in ThisBuild ++= 
//... in ThisProject := 

//libraryDependencies ++= Seq(
//    "pl.project13.scala" % "rainbow" % "0.2"
//)
