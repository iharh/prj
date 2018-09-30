import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.StdWString;

@Platform(include="NativeLibrary.h")
@Namespace("NativeLibrary")
public class NativeLibrary {
    public static class NativeClass extends Pointer {
        static { Loader.load(); }
        public NativeClass() { allocate(); }
        private native void allocate();

        public native @StdWString IntPointer getWString();
    }

    public static void main(String[] args) {
        System.out.println("start");
        try (NativeClass l = new NativeClass()) {
            IntPointer pI = l.getWString();
            int [] arrI = pI.getStringCodePoints();
            for (int i = 0; i < arrI.length; ++i) {
                System.out.println(arrI[i]);
            }
            System.out.println(pI.getString());
        }
        System.out.println("finish");
    }
}
