tasks {
    register("aaa") {
        doLast {
            if (project.hasProperty("isCI")) {
                println("!!! aaa with isCI called")
            } else {
                println("!!! aaa without isCI called")
            }
        }
    }
}
