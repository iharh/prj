package mockiface;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class MockIfaceTest {
    @Test
    public void testMisc() throws Exception {
        SomeIface someIface = mock(SomeIface.class);

        someIface.onSome("abc");
        someIface.onSome("def");

        verify(someIface, times(1)).onSome("abc");
        verify(someIface, times(1)).onSome("def");
    }
}
