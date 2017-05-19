import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec

import com.clarabridge.clbfoma.LibClbFoma;
import com.clarabridge.clbfoma.ClbFomaLoader;

class ClbfomaTests : StringSpec() {
    init {
	"clbfoma lib should be loaded and behave as expected" {
	    val clbFoma = ClbFomaLoader.load()
	    clbFoma shouldNotBe null

	    val pFsm = clbFoma.iface_load_file("bin/morphind")
	    pFsm shouldNotBe null

	    val pApply = clbFoma.apply_init(pFsm)
	    pApply shouldNotBe null

	    var analyzed = clbFoma.apply_up(pApply, "kirim");
	    analyzed shouldBe "kirim<v>_VSA"
	    analyzed = clbFoma.apply_up(pApply, "aku");
	    analyzed shouldBe "aku<v>_VSA"

	    clbFoma.apply_clear(pApply)
	    clbFoma.fsm_destroy(pFsm)
	}
    }
}
