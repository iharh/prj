import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

apply {
    File("${rootProject.projectDir}/scripts").walk()
        .filter { !it.isDirectory() && it.toString().endsWith(".gradle.kts") && !it.toString().endsWith(".swp") }
        .forEach {
            from(it.toString())
        }
}

plugins {
    idea
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_1_8)
        //vcs = "Git"
    }
}

val hello by tasks.creating {
    doLast {
	println(listOf("hello", "world"))
	mySuperFun()
    }
}

val benchEn by tasks.creating {
    doLast {
	val dirMain = "d:/clb/src/main"
	val dirLP = "${dirMain}/lang-packs/english"
	doBench(dirLP)
    }
}

declareMyTask()
