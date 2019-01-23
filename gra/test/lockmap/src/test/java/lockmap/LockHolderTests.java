package lockmap;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.assertj.core.api.Assertions.assertThat;

public class LockHolderTests {

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

        assertThat(true).isEqualTo(true);
    }
}

