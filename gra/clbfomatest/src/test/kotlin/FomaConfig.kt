import io.kotlintest.ProjectConfig

import com.clarabridge.clbfoma.LibClbFoma;
import com.clarabridge.clbfoma.ClbFomaLoader;

import jnr.ffi.Pointer;

object FomaConfig : ProjectConfig() {
    lateinit val clbFoma: LibClbFoma // = ClbFomaLoader.load() // clbFoma shouldNotBe null
    lateinit val pFsm: Pointer // = clbFoma.iface_load_file("bin/morphind") // pFsm shouldNotBe null

    fun getLibFoma() : LibClbFoma { return clbFoma }
    fun getFsm() : Pointer { return pFsm }

    override fun beforeAll() {
	clbFoma = ClbFomaLoader.load() // clbFoma shouldNotBe null
	pFsm = clbFoma.iface_load_file("bin/morphind") // pFsm shouldNotBe null
	//println("beforeAll called !!!")
    }
    override fun afterAll() {
	clbFoma.fsm_destroy(pFsm)
	//println("afterAll called !!!")
    }
}
