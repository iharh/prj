apply {
    File("scripts").walk()
        .filter { !it.isDirectory() }
        .forEach {
            from(it.toString())
        }
}

task("hello") {
    doLast {
    }
}
