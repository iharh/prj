import java.nio.charset.StandardCharsets

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter

val cvt by tasks.creating {
    doLast {
	val inFileName = "d:/dev/notes/book.txt"
        BufferedReader(InputStreamReader(File(inFileName).inputStream(), "Cp866")).use { inR->
            PrintWriter(OutputStreamWriter(File("d:/out.txt").outputStream(), StandardCharsets.UTF_8)).use { outW ->
		while (inR.ready()) {
		    outW.println(inR.readLine())
		}
	    }
	}
    }
}

