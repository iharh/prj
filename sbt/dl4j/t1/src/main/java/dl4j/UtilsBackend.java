package dl4j;

import org.nd4j.linalg.factory.Nd4jBackend;

import java.util.ServiceLoader;
import java.util.ServiceConfigurationError;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsBackend {
    private static final Logger log = LoggerFactory.getLogger(UtilsBackend.class);

    private static void checkBackends() {
        final String cn = "org.nd4j.linalg.netlib.NetlibBlasBackend";
        Class<?> c = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            c = Class.forName(cn, false, cl);
            log.error("Provider {} found", cn);
        } catch (ClassNotFoundException x) {
            log.error("Provider {} not found", cn);
        }

        List<Nd4jBackend> backends = new ArrayList<>(1);
        ServiceLoader<Nd4jBackend> loader = ServiceLoader.load(Nd4jBackend.class);
        try {

            Iterator<Nd4jBackend> backendIterator = loader.iterator();
            while (backendIterator.hasNext()) {
                Nd4jBackend b = backendIterator.next();
                //backends.add();
                log.info("Loaded: {}", b.getClass().getName()); // getSimpleName());
            }

        } catch (ServiceConfigurationError serviceError) {
            log.error("ServiceConfigurationError: " + serviceError.getMessage(), serviceError);
            // a fatal error due to a syntax or provider construction error.
            // backends mustn't throw an exception during construction.
            throw new RuntimeException("failed to process available backends", serviceError);
        }
    }
};    
