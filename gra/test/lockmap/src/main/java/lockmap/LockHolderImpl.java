package lockmap;

import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockHolderImpl {
    private final ConcurrentMap<Long, ReentrantReadWriteLock> locks;

    private static final int   DEFAULT_INIT_CAPACITY                               = 8;
    private static final float DEFAULT_LOAD_FACTOR                                 = 0.75f;
    private static final int   DEFAULT_CONCUR_LEVEL                                = 8;
    private static final ConcurrentReferenceHashMap.ReferenceType DEFAULT_REF_TYPE = ConcurrentReferenceHashMap.ReferenceType.SOFT;

    public LockHolderImpl() {
        this.locks = new ConcurrentReferenceHashMap<>(DEFAULT_INIT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCUR_LEVEL, DEFAULT_REF_TYPE);
    }

    public LockHolderImpl(int concurLevel, ConcurrentReferenceHashMap.ReferenceType refType) {
        this.locks = new ConcurrentReferenceHashMap<>(DEFAULT_INIT_CAPACITY, DEFAULT_LOAD_FACTOR, concurLevel, refType);
    }

    private ReentrantReadWriteLock getLock(long key) {
        return locks.computeIfAbsent(key, k -> new ReentrantReadWriteLock()); // autoboxing
    }

    public void withReadLockAcquired(long ptrLexiconResGrp, LockHolderSupplier supplier) {
        final ReentrantReadWriteLock lock = getLock(ptrLexiconResGrp);
        final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try {
            supplier.get();
        } finally {
            readLock.unlock();
        }
    }

    public void withWriteLockAcquired(long ptrLexiconResGrp, LockHolderSupplier supplier) {
        final ReentrantReadWriteLock lock = getLock(ptrLexiconResGrp);
        final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            supplier.get();
        } finally {
            writeLock.unlock();
        }
    }
}
