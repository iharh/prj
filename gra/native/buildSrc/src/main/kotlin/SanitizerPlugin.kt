import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.language.cpp.tasks.CppCompile
import org.gradle.nativeplatform.tasks.LinkExecutable

import org.gradle.kotlin.dsl.*


class SanitizerPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        val sanitize = extensions.create("sanitize", SanitizeExtension::class)

        tasks {
            register("greet") {
                group = "sample"
                description = "Prints a description of ${project.name}."
                doLast {
                    println("I'm ${project.name}.")
                }
            }
            withType<CppCompile>().configureEach {
                if (isSanitizeAddress(name, project, sanitize)) {
                    compilerArgs.addAll(listOf("-ggdb", "-O0", "-fno-omit-frame-pointer", "-fsanitize=address"))
                }
            }
            withType<LinkExecutable>().configureEach {
                if (isSanitizeAddress(name, project, sanitize)) {
                    linkerArgs.addAll(listOf("-fsanitize=address"))
                }
            }
        }
    }

    private fun isSanitizeAddress(taskName: String, project: Project, sanitize: SanitizeExtension): Boolean {
        println(":${project.name}:$taskName sanitize-address: ${sanitize.address}]") 
        return sanitize.address
    }
}
