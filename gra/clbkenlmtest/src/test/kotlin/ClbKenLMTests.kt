import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

import com.clarabridge.clbkenlm.LibClbKenLM
import com.clarabridge.clbkenlm.ClbKenLMLoader

import jnr.ffi.Memory
import jnr.ffi.Runtime
import jnr.ffi.Pointer

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.ByteBuffer

import java.nio.charset.StandardCharsets.UTF_8

class ClbfomaTests : StringSpec() {
    val myTable = table(
	headers("tag", "result"),
	row("V", -4.3251605),
	row("P", -3.6845271),
	row("V P", -5.83864),
	row("P V", -4.46062)
    )
    init {
	"clbkenlm lib should match result" {
	    val clbKenLM = ClbKenLMLoader.load()

	    val f = File("build.gradle") // "tag.lm.bin"
	    val d = FileUtils.readFileToByteArray(f)
	    val s: Int = d.size

	    val runtime = Runtime.getRuntime(clbKenLM)
	    val ex_message = Memory.allocateDirect(runtime, 1024)

	    val pHandle = clbKenLM.kenlm_init(s, d, ex_message)
	    if (pHandle == null) {
		val msg = ex_message.getString(0, 1024, UTF_8) // Charset.forName("UTF-8")
		"" shouldBe msg
	    }
	    pHandle shouldNotBe null

	    forAll(myTable) { tag, result ->
		val analyzed = clbKenLM.kenlm_query(pHandle, tag)
		analyzed shouldBe (result plusOrMinus 0.001)
	    }

	    clbKenLM.kenlm_clean(pHandle)
	}
    }
}
