import org.apache.commons.lang3.math.NumberUtils

parallelExecution := false

lazy val e1 = taskKey[Unit]("echo task 1")

e1 := {
    println("e1 called:" + (NumberUtils.INTEGER_MINUS_ONE))
} 
