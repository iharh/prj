package fxpool;

//import org.slf4j.LoggerFactory;

//import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.HttpWaitStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;

public class JavaTestContainer<SELF extends JavaTestContainer<SELF>> extends GenericContainer<SELF> {

    public JavaTestContainer() {
        super(new ImageFromDockerfile("myboot", false)
            .withDockerfileFromBuilder(builder ->
                builder.from("java:8")
                    .copy("/tmp/fxpool.jar", "fxpool.jar")
                    //.cmd("cat /foo/test/resources/test-recursive-file.txt")
                    .cmd("java -jar fxpool.jar")
                    .build()
            )
            .withFileFromFile("/tmp/fxpool.jar", new File("./build/libs/fxpool.jar")));  // '.' is the project base directory

        //withClasspathResourceMapping("agent.jar", "/agent.jar", BindMode.READ_ONLY);
        //withClasspathResourceMapping(script, "/app/app.groovy", BindMode.READ_ONLY);

        withCommand("java -jar fxpool.jar");

        withExposedPorts(18080);
        setWaitStrategy(new HttpWaitStrategy().forPath("/v1/hello/"));
        //withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(script)));
    }
/*
    public String getURL() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(getExposedPorts().get(0)); // ? 18080
    }
*/
}
