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

class ClbfomaTests : StringSpec() {
    val myTable = table(
	headers("tag", "result"),
	row("V", -4.3251605),
	row("P", -3.6845271)
    )
    init {
	"clbkenlm lib should match result" {
	    val clbKenLM = ClbKenLMLoader.load()
	    val pHandle = clbKenLM.kenlm_init();
	    pHandle shouldNotBe null

	    forAll(myTable) { tag, result ->
		val analyzed = clbKenLM.kenlm_query(pHandle, tag)
		analyzed shouldBe (result plusOrMinus 0.001)
	    }

	    clbKenLM.kenlm_clean(pHandle)
	}
    }
}
