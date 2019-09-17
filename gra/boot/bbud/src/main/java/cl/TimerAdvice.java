package cl;

import net.bytebuddy.asm.Advice;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class TimerAdvice {
    /**
     * From this  enter method we start the timer and pass that value to exit method and the we getting the time
     * speed for each method
     */
    @Advice.OnMethodEnter
    static long enter(@Advice.Origin String method) throws Exception {
        long start = System.currentTimeMillis();
        return start;
    }

    @Advice.OnMethodExit
    static void exit(@Advice.Origin String method, @Advice.Enter long start) throws Exception {
        long end = System.currentTimeMillis();
        // log.info("{} took {} milliseconds", method, (end - start));
        System.out.println(method + " took " + (end - start) + " milliseconds");
    }
}
