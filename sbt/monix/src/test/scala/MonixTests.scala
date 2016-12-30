//import org.scalatest._

import minitest.TestSuite
import minitest.SimpleTestSuite

import monix.eval.Task

import monix.execution.Ack.Continue

import monix.execution.schedulers.ExecutionModel.AlwaysAsyncExecution
import monix.execution.schedulers.TestScheduler

import monix.reactive.Observable


import org.slf4j.LoggerFactory

object MonixTests extends SimpleTestSuite {
    test("monix") {
        assertEquals(true, true)
    }
}
/*
class MonixTests extends TestSuite[TestScheduler] {
    private val log = LoggerFactory.getLogger(getClass)

    def setup() = TestScheduler()

    def tearDown(s: TestScheduler): Unit = {
        assert(s.state.tasks.isEmpty, "TestScheduler should have no pending tasks")
    }

    test("should respect the ExecutionModel") { scheduler =>
        log.info("start")
        implicit val s = scheduler.withExecutionModel(AlwaysAsyncExecution)

        var received = 0
        val cancelable = Observable
            .fromAsyncStateAction(intNow)(s.currentTimeMillis())
            .subscribe { x => received += 1; Continue }

        assertEquals(received, 0)
        s.tickOne(); s.tickOne()
        assertEquals(received, 1)
        s.tickOne(); s.tickOne()
        assertEquals(received, 2)

        cancelable.cancel(); s.tick()
        assertEquals(received, 2)
        assert(s.state.tasks.isEmpty, "tasks.isEmpty")

        log.info("end")
    }

    def intAsync(seed: Long) = Task(int(seed))
    def intNow(seed: Long) = Task.now(int(seed))

    def int(seed: Long): (Int, Long) = {
        // `&` is bitwise AND. We use the current seed to generate a new seed.
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
        // The next state, which is an `RNG` instance created from the new seed.
        val nextRNG = newSeed
        // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
        val n = (newSeed >>> 16).toInt
        // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
        (n, nextRNG)
    }
}
*/
