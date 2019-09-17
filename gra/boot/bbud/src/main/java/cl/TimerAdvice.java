package cl;

import net.bytebuddy.asm.Advice;

import org.ehcache.core.spi.store.Store;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class TimerAdvice {

    @Advice.OnMethodEnter
    static long enter(@Advice.Argument(1) Store.ValueHolder<?> vh) throws Exception {
        final Method methodGet = vh.getClass().getDeclaredMethod("get");
        final Class<?> returnClass = methodGet.getReturnType();
        if (returnClass.isAssignableFrom(ResourceValue.class)) {
            return 1;
        }
        return 0;
    }

    @Advice.OnMethodExit
    static void exit(@Advice.Enter long nativeSize,
            @Advice.Return(readOnly = false) Long result) throws Exception {
        System.out.println("result: " + result + ", nativeSize: " + nativeSize);
        result += nativeSize;
    }
}
