import io.kotlintest.ProjectConfig

import com.clarabridge.clbkenlm.LibClbKenLM
import com.clarabridge.clbkenlm.ClbKenLMLoader

import jnr.ffi.Memory
import jnr.ffi.Runtime
import jnr.ffi.Pointer

import org.apache.commons.io.FileUtils

import java.io.File

import java.nio.charset.StandardCharsets.UTF_8

object KenLMConfig : ProjectConfig() {
    public const val EX_MSG_SIZE = 2048

    lateinit var clbKenLM: LibClbKenLM
    lateinit var pModel: Pointer

    fun getLibKenLM() : LibClbKenLM = clbKenLM
    fun getModel() : Pointer = pModel

    override fun beforeAll() {
	clbKenLM = ClbKenLMLoader.load()

        val f = File("tag.lm.bin") // "tag.lm.bin", "build.gradle"
        val d = FileUtils.readFileToByteArray(f)
        val s: Int = d.size

        val runtime = Runtime.getRuntime(clbKenLM)
        val exMsg = Memory.allocateDirect(runtime, EX_MSG_SIZE)

        pModel = clbKenLM.kenlm_init(s, d, EX_MSG_SIZE, exMsg)
        if (pModel.address() == 0L) {
            val msg = exMsg.getString(0, KenLMConfig.EX_MSG_SIZE, UTF_8)
            //throw IllegalArgumentException(exMsg)
        }
    }
    override fun afterAll() {
	clbKenLM.kenlm_clean(pModel)
    }
}
