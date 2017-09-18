package sprestapi.configs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
//@Import({})
@ComponentScan("sprestapi.controllers")
public class RestAppConfig {
}
