import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

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
	    val clbKenLM = KenLMConfig.getLibKenLM()

            val pModel = KenLMConfig.getModel()
	    pModel shouldNotBe null

	    forAll(myTable) { tag, result ->
		val analyzed = clbKenLM.kenlm_query(pModel, tag)
		analyzed shouldBe (result plusOrMinus 0.001)
	    }
	}
    }
}
