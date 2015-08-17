package dj;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;

import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerTest {
    private static final Logger log = LoggerFactory.getLogger(DockerTest.class);
    
    @Test
    public void testDocker() throws Exception {
        log.debug("begin");

        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
            .withVersion("1.16")
            .withUri("http://platform-dev:2375")
            //.withUri("tcp://platform-dev:2375") // can be also set externally via DOCKER_HOST
//            .withUsername("dockeruser")
//            .withPassword("ilovedocker")
//            .withEmail("dockeruser@github.com")
//            .withServerAddress("https://index.docker.io/v1/")
//            .withDockerCertPath("/home/user/.docker")
            .build();

        DockerClient docker = DockerClientBuilder.getInstance(config).build();

        Info info = docker.infoCmd().exec();

        assertNotNull(info);

        log.info("info: {}", info.toString());

        assertTrue(true);
    }
}
