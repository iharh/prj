package fxpool;

//import org.slf4j.LoggerFactory;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;

public class JavaTestContainer<SELF extends JavaTestContainer<SELF>> extends GenericContainer<SELF> {

    public JavaTestContainer() { // (String script)

        super(new ImageFromDockerfile("myboot", false)
            .withFileFromFile("fxpool.jar", new File("build/libs/fxpool.jar"))
            .withDockerfileFromBuilder(builder -> {
                builder
                    .from("java:8")
                    //.copy("service.war", "/usr/local/tomcat/webapps/my-service.war")
                    .build();
            }));

        //withClasspathResourceMapping("agent.jar", "/agent.jar", BindMode.READ_ONLY);
        //withClasspathResourceMapping(script, "/app/app.groovy", BindMode.READ_ONLY);

        // Cache Grapes
        //addFileSystemBind(System.getProperty("user.home") + "/.groovy", "/root/.groovy/", BindMode.READ_WRITE);

        //withEnv("JAVA_OPTS", "-javaagent:/agent.jar");

        //withCommand("/opt/groovy/bin/groovy /app/app.groovy");
        withCommand("java -jar fxpool.jar");

        //withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(script)));

        withExposedPorts(18080);
    }

    public String getURL() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(getExposedPorts().get(0)); // ? 18080
    }
}
