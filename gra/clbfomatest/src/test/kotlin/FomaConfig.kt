import io.kotlintest.ProjectConfig

import com.clarabridge.clbfoma.LibClbFoma;
import com.clarabridge.clbfoma.ClbFomaLoader;

import jnr.ffi.Pointer;

import org.apache.commons.io.FileUtils

import java.io.File

object FomaConfig : ProjectConfig() {
    lateinit var clbFoma: LibClbFoma
    lateinit var pIO: Pointer
    lateinit var pFsm: Pointer

    fun getLibFoma() : LibClbFoma { return clbFoma }
    fun getFsm() : Pointer { return pFsm }

    override fun beforeAll() {
	clbFoma = ClbFomaLoader.load()
	//pIO = clbFoma.io_init_text_file("bin/morphind")
	val f = File("bin/morphind")
	val d = FileUtils.readFileToByteArray(f)
	val s: Int = d.size
	pIO = clbFoma.io_init_buf_ptr(s, d);

	pFsm = clbFoma.iface_load_buf(pIO)
	//println("beforeAll called !!!")
    }
    override fun afterAll() {
	clbFoma.io_free(pIO)
	clbFoma.fsm_destroy(pFsm)
	//println("afterAll called !!!")
    }
}
