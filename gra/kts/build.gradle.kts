apply {
    File("scripts").walk()
        .filter { !it.isDirectory() && it.toString().endsWith(".gradle.kts") && !it.toString().endsWith(".swp") }
        .forEach {
            from(it.toString())
        }
}

val hello by tasks.creating {
    doLast {
        //mySuperFun()
	println(listOf("hello", "world"))
	mySuperFun()
    }
}

declareMyTask()
