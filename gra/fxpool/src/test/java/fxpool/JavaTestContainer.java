package fxpool;

import org.slf4j.LoggerFactory;

//import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.HttpWaitStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;

import feign.Feign;
import feign.Logger;
import feign.RequestLine;
import feign.Response;

import feign.slf4j.Slf4jLogger;

import java.io.File;

import lombok.Getter;

public class JavaTestContainer<SELF extends JavaTestContainer<SELF>> extends GenericContainer<SELF> {

    @Getter(lazy = true)
    private final Client client = Feign
        .builder()
        .logLevel(Logger.Level.FULL)
        .logger(new Slf4jLogger()) // Client.class
        .target(Client.class, getURL());

    public JavaTestContainer() {
        super(new ImageFromDockerfile("myboot", false)
            .withDockerfileFromBuilder(builder ->
                builder.from("java:8")
                    .copy("/tmp/fxpool.jar", "fxpool.jar")
                    .cmd("java -jar fxpool.jar")
                    .build()
            )
            .withFileFromFile("/tmp/fxpool.jar", new File("./build/libs/fxpool.jar")));  // '.' is the project base directory

        //withClasspathResourceMapping("agent.jar", "/agent.jar", BindMode.READ_ONLY);
        //withClasspathResourceMapping(script, "/app/app.groovy", BindMode.READ_ONLY);

        withCommand("java -jar fxpool.jar");

        withExposedPorts(18080);
        setWaitStrategy(new HttpWaitStrategy().forPath("/v1/hello/"));
        withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(JavaTestContainer.class)));
    }

    public String getURL() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(getExposedPorts().get(0)); // ? 18080
    }

    public interface Client { 
        @RequestLine("GET /v1/hello/")
        Response getHello();
    }
}
