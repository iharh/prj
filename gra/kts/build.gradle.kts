apply {
    File("scripts").walk()
        .filter { !it.isDirectory() }
        .forEach {
            // println("including " + it.toString())
            from(it.toString())
        }
}

task("hello") {
    doLast {
    }
}
