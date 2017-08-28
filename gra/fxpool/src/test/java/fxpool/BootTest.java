package fxpool;

import org.junit.ClassRule;
import org.junit.Test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.output.WaitingConsumer;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;
import org.testcontainers.containers.wait.HttpWaitStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.rnorth.visibleassertions.VisibleAssertions.assertTrue;

public class BootTest {
    @ClassRule
    public static JavaTestContainer javaTC = new JavaTestContainer();

    @Test
    public void simpleRecursiveFileTest() throws Exception { // TimeoutException
        /*
        WaitingConsumer wait = new WaitingConsumer();
        final ToStringConsumer toStringConsumer = new ToStringConsumer();
        GenericContainer container = new GenericContainer(
            new ImageFromDockerfile()
                .withDockerfileFromBuilder(builder ->
                    builder.from("java:8")
                        .copy("/tmp/fxpool.jar", "fxpool.jar")
                        .cmd("java -jar fxpool.jar")
                        .build()
                )
                .withFileFromFile("/tmp/fxpool.jar", new File("./build/libs/fxpool.jar")));  // '.' is the project base directory
        container
            .withExposedPorts(18080) // -> 18080 at guest -> to random port at host-box
            .setWaitStrategy(new HttpWaitStrategy().forPath("/v1/hello/"))
        ;
        */

        //container.start();
        //wait.waitUntilEnd(360, TimeUnit.SECONDS); // need to wait here

        //final String results = toStringConsumer.toUtf8String();
        //assertTrue("The container has a file that was copied in via a recursive copy", results.contains("Used for DirectoryTarResourceTest"));
    }
}
