package ehc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Util class allowing to access the {@code PROVE} used by {@code ThreadLocalRandom}. And to get
 * access to unsafe.
 */
class ThreadLocalRandomUtil {

  static final Unsafe UNSAFE = getSMU();
  private static final long PROBE;

  static {
    try {
      Class<?> tk = Thread.class;
      PROBE = UNSAFE.objectFieldOffset
        (tk.getDeclaredField("threadLocalRandomProbe"));
    } catch (Exception e) {
      throw new Error(e);
    }
  }

  static Unsafe getSMU() {
    try {
      return sun.misc.Unsafe.getUnsafe();
    } catch (SecurityException tryReflectionInstead) {
      // ignore
    }
    try {
      return java.security.AccessController.doPrivileged
        ((PrivilegedExceptionAction<Unsafe>) () -> {
          Class<sun.misc.Unsafe> k = sun.misc.Unsafe.class;
          for (Field f : k.getDeclaredFields()) {
            f.setAccessible(true);
            Object x = f.get(null);
            if (k.isInstance(x))
              return k.cast(x);
          }
          throw new NoSuchFieldError("the Unsafe");
        });
    } catch (java.security.PrivilegedActionException e) {
      throw new RuntimeException("Could not initialize intrinsics", e.getCause());
    }
  }

  static final int getProbe() {
    return UNSAFE.getInt(Thread.currentThread(), PROBE);
  }

  static final int advanceProbe(int probe) {
    probe ^= probe << 13;   // xorshift
    probe ^= probe >>> 17;
    probe ^= probe << 5;
    UNSAFE.putInt(Thread.currentThread(), PROBE, probe);
    return probe;
  }

  static final void localInit() {
    // This will kick an init. In ConcurrentHashMap, it is only called after having check if the probe is there, so
    // there shouldn't be any drawback to do it that way
    ThreadLocalRandom.current();
  }

  /** Optimized form of: key + "=" + val */
  static String mapEntryToString(Object key, Object val) {
    final String k, v;
    final int klen, vlen;
    final char[] chars =
      new char[(klen = (k = objectToString(key)).length()) +
               (vlen = (v = objectToString(val)).length()) + 1];
    k.getChars(0, klen, chars, 0);
    chars[klen] = '=';
    v.getChars(0, vlen, chars, klen + 1);
    return new String(chars);
  }

  private static String objectToString(Object x) {
    // Extreme compatibility with StringBuilder.append(null)
    String s;
    return (x == null || (s = x.toString()) == null) ? "null" : s;
  }
}
