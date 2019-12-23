package srv.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //.cors().disable()
            //.csrf().disable()
            //.sessionManagement().disable()
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                // single-quote parse problems
                .antMatchers("/hello").access("#oauth2.hasScope('cx-designer.api[group=''lexicons-nlpsvc-export'']')")
                // .anyRequest()
                    //.fullyAuthenticated()
                    // to allow access without auth
                    //.permitAll() 
        ;
    }
}
