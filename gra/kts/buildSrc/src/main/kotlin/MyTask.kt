import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
// TODO: investigate *
import org.gradle.kotlin.dsl.*

open class MyTask : DefaultTask() {
    init {
        group = "My"
        description = "Prints a description of ${project.name}."
    }
    @TaskAction
    fun run() {
        println("MyTask - I'm ${project.name}")
    }
}

fun Project.declareMyTask()
    = task<MyTask>("myTask")
val Project.myTask: MyTask
    get() = tasks["myTask"] as MyTask
