package my.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

//import org.springframework.boot.context.properties.ConfigurationProperties;

//import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
@EnableResourceServer
public class ResourceSecurityConfig extends ResourceServerConfigurerAdapter {
    /*
    @Bean
    @ConfigurationProperties("security.oauth2.localas.resource")
    public ClientCredentialsResourceDetails localAuthServerResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }
    */

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resource");
    }

    @Bean
    public RemoteTokenServices LocalTokenService() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        // http://localhost:8091/oauth/token
        tokenService.setCheckTokenEndpointUrl("http://localhost:8091/oauth/check_token");
        tokenService.setClientId("nlpsvc");
        tokenService.setClientSecret("devsecret");
        return tokenService;
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

    // new DefaultOAuth2ClientContext()
}
