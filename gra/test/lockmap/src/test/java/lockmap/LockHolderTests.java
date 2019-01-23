package lockmap;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.assertj.core.api.Assertions.assertThat;

public class LockHolderTests {
    private static final int ITERATION_CNT = 1000000;

    @AllArgsConstructor
    @Getter
    public class NonAtomicInt {
        private int value;

        public void increment() {
            this.value++;
        }
    }

    @Test
    void testLock() throws Exception {
        final LockHolderImpl holder = new LockHolderImpl();

        NonAtomicInt nonAtomicInt = new NonAtomicInt(0);

        IntStream.range(0, ITERATION_CNT)
             .boxed()
             .parallel()
             .forEach(i -> holder.withWriteLockAcquired(0, nonAtomicInt::increment));

        Thread.sleep(1000);

        assertThat(nonAtomicInt.getValue()).isEqualTo(ITERATION_CNT);
    }
}
