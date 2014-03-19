import scala.Console._

// http://www.scala-sbt.org/release/docs/Detailed-Topics/Parallel-Execution.html
//concurrentRestrictions in Global := Seq(
//  Tags.limitAll(1)
//  Tags.limit(Tags.CPU, 2),
//  Tags.limit(Tags.Network, 10),
//  Tags.limit(Tags.Test, 1),
//)

lazy val e0 = taskKey[Unit]("Echo some staff task 0")
lazy val e1 = taskKey[Unit]("Echo some staff task 1")
lazy val e2 = taskKey[Unit]("Echo some staff task 2")
lazy val e3 = taskKey[Unit]("Echo some staff task 3")

e1 := {
    println("hello e1")
}

e2 := {
    println("hello e2")
    error("this is some error abc")
}

e3 := {
    println("hello e3")
    val ttt = "abc"
    println(s"[${GREEN}run-task$RESET] $ttt") // $taskName
}

e0 := Def.sequentialTask {
    println("hello e0 start")
    val _1 = e1.value
    val _2 = e2.value
    val _3 = e3.value
    println("hello e0 finish")
}.value

// e0 <<= e0.dependsOn(e3, e2, e1)

addCommandAlias("eall", ";e1;e2;e3")
