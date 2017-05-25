import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.properties.headers;
import io.kotlintest.properties.row;
import io.kotlintest.properties.table;
import io.kotlintest.properties.forAll;
import io.kotlintest.specs.StringSpec

import com.clarabridge.clbfoma.LibClbFoma;
import com.clarabridge.clbfoma.ClbFomaLoader;

class ClbfomaTests : StringSpec() {
    val maxIterations = 1; // 1000000
    val myTable = table(
	headers("token", "result"),
	row("kirim", "kirim<v>_VSA"),
	row("aku"  , "aku<v>_VSA")
    )
    init {
	"clbfoma lib should be loaded and behave as expected" {
	    val clbFoma = FomaConfig.getLibFoma()
	    val pFsm = FomaConfig.getFsm()

	    val pApply = clbFoma.apply_init(pFsm)
	    pApply shouldNotBe null

	    for (i in 1..maxIterations) {
		forAll(myTable) { token, result ->
		    val analyzed = clbFoma.apply_up(pApply, token)
		    analyzed shouldBe result
		}
	    }

	    clbFoma.apply_clear(pApply)
	}.config(threads = 8, invocations = 8)
    }
    init {
	"multi-output" {
	    val clbFoma = FomaConfig.getLibFoma()
	    clbFoma shouldNotBe null
	    val pFsm = FomaConfig.getFsm()
	    pFsm shouldNotBe null

	    val pApply = clbFoma.apply_init(pFsm)
	    val a1 = clbFoma.apply_up(pApply, "aku")
	    a1 shouldBe "aku<v>_VSA"
	    val a2 = clbFoma.apply_up(pApply, null)
	    a2 shouldBe "a<f>_F--+aku<p>_PS1"
	    val a3 = clbFoma.apply_up(pApply, null)
	    a3 shouldBe "aku<p>_PS1"
	    val a4 = clbFoma.apply_up(pApply, null)
	    a4 shouldBe null

	    clbFoma.apply_clear(pApply)
	}.config(enabled = true)
    }
}
