import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
//import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

import kotlin.test.assertEquals

//import org.junit.runner.RunWith
//import org.junit.platform.runner.JUnitPlatform

//@RunWith(JUnitPlatform::class)
object SimpleSpec: Spek({
    describe("a calculator") {
        val calculator = 3

        on("addition") {
            it("should return the result of adding the first number to the second number") {
                assertEquals(4, calculator + 1)
            }
        }

        on("subtraction") {
            it("should return the result of subtracting the second number from the first number") {
                assertEquals(2, calculator - 1)
            }
        }
    }
})
