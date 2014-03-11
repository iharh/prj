package mdm.avail;

public class AvailChecker {
    private long lastErrTime = -1L;
    private long thresholdMillis;

    public AvailChecker(long thresholdMillis) {
        this.thresholdMillis = thresholdMillis;
    }

    public synchronized boolean isAvailable() {
        return (getCurTime() - lastErrTime) >= thresholdMillis;
    }

    public synchronized void signallError() {
        lastErrTime = getCurTime();
    }

    private long getCurTime() {
        return System.currentTimeMillis();
    }
}

