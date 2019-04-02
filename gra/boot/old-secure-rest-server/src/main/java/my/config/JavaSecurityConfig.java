package my.config;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;

@Configuration
@Import({ WebSecurityConfig.class, MethodSecurityConfig.class })
public class JavaSecurityConfig { 
}
