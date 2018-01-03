import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import krangl.*

apply {
    File("${rootProject.projectDir}/scripts").walk()
        .filter { !it.isDirectory() && it.toString().endsWith(".gradle.kts") && !it.toString().endsWith(".swp") }
        .forEach {
            from(it.toString())
        }
}

plugins {
    // `kotlin-dsl` // just to support compile in dependencies
    idea
}

val shlibcfg by configurations.creating
val lpcfg by configurations.creating

val nexusResolveUrl = rootProject.extra["nexus.repo.resolve"] as String

repositories {
    jcenter()
    maven (url = rootProject.extra["nexus.repo.resolve"] as String)

    // CB ivy artifacts resolver
    ivy (url = nexusResolveUrl) {
        name = "cb_common"
        ivyPattern("${nexusResolveUrl}/[organisation]/[module]/[revision]/ivy.xml")
        artifactPattern("${nexusResolveUrl}/[organisation]/[module]/[revision]/[artifact](.[ext])")
    }

    ivy (url = nexusResolveUrl) {
        name = "cb_lp"
        ivyPattern("${nexusResolveUrl}/[organisation]/[module]-linux/[revision]/ivy.xml")
        artifactPattern("${nexusResolveUrl}/[organisation]/[module]-linux/[revision]/[artifact](.[ext])")
    }
}

val fullFxVer = rootProject.extra["fx.version"] as String
val fullLpEnVer = rootProject.extra["lp.english.version"] as String

dependencies {
    // compile("de.mpicbg.scicomp:krangl:0.6")

    // shared libs
    shlibcfg(group = "Clarabridge", name = "FX-windows-x64", version = fullFxVer, configuration = "publish")

    lpcfg(group = "Clarabridge", name = "LP-English", version = fullLpEnVer, configuration = "publish")
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_1_8)
        //vcs = "Git"
    }
}

val dirMain = "d:/clb/src/main"
val dirLP = "${dirMain}/lang-packs/english"
val dirBuild = "${dirLP}/.build"
val dirTarget = "${dirBuild}/target"
val dirFxLib = "${dirTarget}/lib"
val dirTargetData = "${dirTarget}/data"
//val dirResBase = "${dirLP}/resources/target"

val dirDataSetPerf = "${dirLP}/datasets/Performance"
val fileNameArchivedDataSet = "${dirDataSetPerf}/10000 files of 2-3 Kb.zip"
//val fileNameArchivedDataSet = "${dirDataSetPerf}/100f.zip"

val dirReport = "${dirBuild}/reports"

val fileNameLPCfg = "${dirLP}/resources/config/config.xml"

val dirTargetLp = "${dirTarget}/lp"
val dirTargetLpResBase = "${dirTargetLp}/fx"

tasks {
    "cpFX" {
	doLast {
	    delete(dirFxLib)
            copy {
                from(shlibcfg) {
		    rename { filename -> filename.replace("-${fullFxVer}", "") }
		}
		include("*.dll") // TODO: win/lin
		include("*.jar")
                into(dirFxLib)
            }
	}
    }
    "cpLP" {
	doLast {
	    delete(dirTargetLp)
            copy {
                from(lpcfg)
                into(dirTargetLp)
            }
	    fileTree(dirTargetLp).files.forEach { artifact ->
		println("Unpacking ${artifact.name}") // setup_en_7.3.2.0-7.3.2.0.zip
		copy {
		    from(zipTree(artifact))
		    include("fx/*/**")
		    into(dirTargetLp)
		}
	    }
	}
    }
    "bench" {
	// dependsOn("cpFX")
	// dependsOn("cpLP")
	doLast {
	    prepareBenchData(dirTargetData, fileNameArchivedDataSet)
	    val cpJfx = fileTree(dirFxLib) {
		include("**/*.jar")
	    }
	    //cpJfx.forEach { println(it) }

	    //delete(dirReport)
	    //mkdir(dirReport)
	    for (i in 1..5) {
		doGenLogCfg("log4j-bench.jade", dirFxLib, "${dirReport}/${i}")
		javaexec {
		    main = "com.clarabridge.fx.Main"
		    classpath = cpJfx // shlibcfg
		    setWorkingDir(dirFxLib)
		    setIgnoreExitValue(true)
		    // systemProperty("log4j.debug", "true")
		    systemProperty("log4j.configuration", "file:/${dirFxLib}/log4j.xml")
		    systemProperty("java.library.path", dirFxLib)
		    setArgs(listOf(
			"-config", fileNameLPCfg,
			"-resbasedir", dirTargetLpResBase, // dirResBase
			dirTargetData
		    ))
		}
	    }
	}
    }
    "stat" {
        val userHome = System.getProperty("user.home") // user.dir
        fxBenchParseGenCSV("a", "${userHome}/Downloads/7.5.0.0-7.3.0.0")

        val df = DataFrame.fromCSV("data/a0.csv")
        val groupedDf: DataFrame = df.groupBy("name")
        //println("Grouped DF:")
        //groupedDf.glimpse() // ??? empty

        val summarizedDf = groupedDf.summarize(
            "minall"  `=` { it["all"].min()  },
            "maxall"  `=` { it["all"].max()  },
            "meanall" `=` { it["all"].mean() },
            "mincpu"  `=` { it["cpu"].min()  },
            "maxcpu"  `=` { it["cpu"].max()  },
            "meancpu" `=` { it["cpu"].mean() }
        )
        println("Summarized DF:")
        summarizedDf.glimpse()
        summarizedDf.writeCSV("data/a1.csv")
    }
    "hello" {
	doLast {
	    println("fullFxVer: ${fullFxVer}")
	    for (i in 1..3) {
		println(listOf("hello", "world", i))
	    }
	    mySuperFun(dirReport)
	}
    }
}

declareMyTask()
