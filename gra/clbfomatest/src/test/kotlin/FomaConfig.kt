import io.kotlintest.ProjectConfig

import com.clarabridge.clbfoma.LibClbFoma;
import com.clarabridge.clbfoma.ClbFomaLoader;

import jnr.ffi.Pointer;

object FomaConfig : ProjectConfig() {
    lateinit var clbFoma: LibClbFoma
    lateinit var pFsm: Pointer

    fun getLibFoma() : LibClbFoma { return clbFoma }
    fun getFsm() : Pointer { return pFsm }

    override fun beforeAll() {
	clbFoma = ClbFomaLoader.load()
	pFsm = clbFoma.iface_load_file("bin/morphind")
	//println("beforeAll called !!!")
    }
    override fun afterAll() {
	clbFoma.fsm_destroy(pFsm)
	//println("afterAll called !!!")
    }
}
