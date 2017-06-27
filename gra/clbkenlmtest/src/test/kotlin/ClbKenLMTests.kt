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

import org.apache.commons.io.FileUtils

import java.io.File

class ClbfomaTests : StringSpec() {
    val myTable = table(
	headers("tag", "result"),
	row("V", -4.3251605),
	row("P", -3.6845271)
    )
    init {
	"clbkenlm lib should match result" {
	    val clbKenLM = ClbKenLMLoader.load()

	    val f = File("tag.lm.bin")
	    val d = FileUtils.readFileToByteArray(f)
	    val s: Int = d.size

	    val pHandle = clbKenLM.kenlm_init(s, d)
	    pHandle shouldNotBe null

	    forAll(myTable) { tag, result ->
		val analyzed = clbKenLM.kenlm_query(pHandle, tag)
		analyzed shouldBe (result plusOrMinus 0.001)
	    }

	    clbKenLM.kenlm_clean(pHandle)
	}
    }
}
