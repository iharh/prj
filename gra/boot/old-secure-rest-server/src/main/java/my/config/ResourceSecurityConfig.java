package my.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@EnableResourceServer
public class ResourceSecurityConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resource");
    }

    @Bean
    @ConfigurationProperties("security.oauth2.localas.resource")
    public RemoteTokenServices localTokenService() {
        return new RemoteTokenServices();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/v1/hello") // , "/some_other"
                    .authenticated()
            .and()
                .csrf().disable()
        ;
    }
}
