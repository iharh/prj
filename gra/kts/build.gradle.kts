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

val shlibcfg by configurations.creating

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
}

val fullFxVer = rootProject.extra["fx.version"] as String

dependencies {
    // shared libs
    shlibcfg(group = "Clarabridge", name = "FX-windows-x64", version = fullFxVer, configuration = "publish")
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
val dirResBase = "${dirLP}/resources/target"

val dirDataSetPerf = "${dirLP}/datasets/Performance"
//val fileNameArchivedDataSet = "${dirDataSetPerf}/10000 files of 2-3 Kb.zip"
val fileNameArchivedDataSet = "${dirDataSetPerf}/100f.zip"

val dirReport = "${dirBuild}/reports"

val fileNameLPCfg = "${dirLP}/resources/config/config.xml"

tasks {
    "hello" {
	doLast {
	    println("fullFxVer: ${fullFxVer}")
	    println(listOf("hello", "world"))
	    mySuperFun()
	}
    }
    "cpSharedLibs" {
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
    "genLogCfg" {
	doLast {
	    delete(dirReport)
	    mkdir(dirReport)
	    doGenLogCfg("log4j-bench.jade", dirFxLib, dirReport)
	}
    }
    "benchEn" {
	dependsOn("cpSharedLibs")
	dependsOn("genLogCfg")
	doLast {
	    prepareBenchData(dirTargetData, fileNameArchivedDataSet)
	    javaexec {
		main = "com.clarabridge.fx.Main"
		classpath = shlibcfg
		setWorkingDir(dirFxLib)
		setIgnoreExitValue(true)
		// systemProperty("log4j.debug", "true")
		systemProperty("log4j.configuration", "file:/${dirFxLib}/log4j.xml")
		systemProperty("java.library.path", dirFxLib)
		setArgs(listOf(
		    "-config", fileNameLPCfg,
		    "-resbasedir", dirResBase,
		    dirTargetData
		))
	    }
	}
    }
}

// java.exe -Djava.library.path=d:/clb/src/main/lang-packs/english/.build/target/lib -Dlog4j.configuration=d:/clb/src/main/lang-packs/english/.build/target/lib/log4j.xml -Dfile.encoding=windows-1252 -Duser.country=US -Duser.language=en -Duser.variant 

declareMyTask()
