package dummy;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.StdString;

@Properties(target = "dummy.DummyProviderLib", value = {
    @Platform(include = "dummy_lib.h")
    //, @Platform(value = "windows-x86_64", preload = { "icudt49", "icuuc49", "icuin49", "icuio49" })
    //, @Platform(value = "linux-x86_64", preload = { "icudata", "icuuc", "icui18n", "icuio" })
})
@Namespace("dummy")
public class DummyProviderLib {

    public static class DummyProviderImpl extends Pointer implements DummyProvider {
        static {
            // load(getCallerClass(2), loadProperties(), Loader.pathsFirst);
            Loader.load(DummyProviderImpl.class);
        }

        public DummyProviderImpl() { allocate(); }
        private native void allocate();

        // custom part
        private native @StdString String processText0(@StdString String text);

        @Override
        public String processText(String text) {
            return processText0(text);
        }
    }
}
