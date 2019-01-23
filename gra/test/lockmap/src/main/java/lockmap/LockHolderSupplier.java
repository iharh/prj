package lockmap;

@FunctionalInterface
public interface LockHolderSupplier<T> {
    void get();
}
