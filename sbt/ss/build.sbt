parallelExecution := false

lazy val e1 = taskKey[Unit]("echo task 1")

e1 := {
    println("e1 called:" + (1 << 11))
} 
