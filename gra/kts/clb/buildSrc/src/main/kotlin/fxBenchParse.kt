import krangl.*

import java.nio.file.Files
import java.nio.file.Paths

import java.io.File

import java.util.stream.Collectors
import java.util.stream.Stream

val recName = "All modules:"
val recSuffix = "sec"

data class BenchStat(val all: Double, val cpu: Double)

private fun lineToDouble(line: String): Double =
    line.substring(recName.length, line.length - recSuffix.length).trim().toDouble()

private fun fxBenchParseReport(fileNameBench: File): BenchStat {
    val sectionAllName = "Total for all files:"
    var sectionAllFound = false

    val sectionCPUName = "Total CPU processing time:"
    var sectionCPUFound = false

    var totalAll: Double = 0.0
    var totalCPU: Double = 0.0

    fileNameBench.bufferedReader().use {
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

private fun fxBenchParseReports(dirNameReports: String): List<BenchStat> {
    val result = Files.list(Paths.get(dirNameReports))
        .filter {
            it.toFile().isDirectory()
        }.map {
            val fileNameBench = it.resolve("benchmark-time-datasetname.txt")
            fxBenchParseReport(fileNameBench.toFile())
        }.collect(Collectors.toList<BenchStat>())
    result.forEach { println("all: ${it.all} cpu: ${it.cpu}") }
    return result
}

fun fxBenchParseGenCSV(serie: Int, startLetter: Char, dirNameReportsBase: String, reportNames: Array<String>) {
    var letter = startLetter
    for (reportName in reportNames) {
        val dirNameReports = "${dirNameReportsBase}/${reportName}"
        val nameUp = letter.toUpperCase()
        val nameDown = letter.toLowerCase()
        letter = letter.inc()

        val s = fxBenchParseReports(dirNameReports)
        s.forEach { println("all: ${it.all} cpu: ${it.cpu}") }
        val df = s.asDataFrame { mapOf( "name" to nameUp, "all" to it.all, "cpu" to it.cpu) }
        println("Original DF:")
        df.glimpse() // df.print()
        df.writeCSV("data/${nameDown}${serie}.csv")
    }
}
