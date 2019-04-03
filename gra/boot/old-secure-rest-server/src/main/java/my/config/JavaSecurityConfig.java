package my.config;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;

@Configuration
@Import({ ResourceSecurityConfig.class, MethodSecurityConfig.class })
public class JavaSecurityConfig { 
}
