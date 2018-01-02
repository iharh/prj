import java.io.File

val recName = "All modules:"
val recSuffix = "sec"

data class BenchStat(val all: Double, val cpu: Double)

fun fxBenchParse(fileNameBench: String): BenchStat {
    val sectionAllName = "Total for all files:"
    var sectionAllFound = false

    val sectionCPUName = "Total CPU processing time:"
    var sectionCPUFound = false

    var totalAll: Double = 0.0
    var totalCPU: Double = 0.0

    File(fileNameBench).bufferedReader().use {
        it.forEachLine { line ->
            if (line.startsWith(sectionAllName)) {
                sectionAllFound = true
            }
            if (line.startsWith(sectionCPUName)) {
                sectionCPUFound = true
            }
            if (sectionAllFound && line.startsWith(recName)) {
                totalAll = lineToDouble(line)
                sectionAllFound = false
            }
            if (sectionCPUFound && line.startsWith(recName)) {
                totalCPU = lineToDouble(line)
                sectionCPUFound = false
            }
        }
    }
    return BenchStat(totalAll, totalCPU)
}

private fun lineToDouble(line: String): Double =
    line.substring(recName.length + 1, line.length - recSuffix.length - 1).toDouble()
