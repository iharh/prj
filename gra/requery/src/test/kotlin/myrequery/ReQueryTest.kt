package myrequery

import io.kotlintest.specs.FunSpec

class ReQueryTest : FunSpec() {
    init {
        test("String.length") {
	    "sammy".length shouldBe 5
            "".length shouldBe 0
        }
    }
}
