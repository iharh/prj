package cl.rtoauth2.conf;

// import com.clarabridge.ingestion.messagerouter.spring.CompressingClientHttpRequestInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
// import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
// import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {
    // private final ClientHttpRequestInterceptor ribbonInterceptor;

    //public ApplicationConfiguration(ClientHttpRequestInterceptor ribbonInterceptor) {
    //    this.ribbonInterceptor = ribbonInterceptor;
    //}

    @Bean
    public OAuth2RestTemplate restTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext clientContext) {
        log.info("enter restTemplate");

        ClientCredentialsAccessTokenProvider tokenProvider = new ClientCredentialsAccessTokenProvider();
        // tokenProvider.setInterceptors(Collections.singletonList(ribbonInterceptor));

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource, clientContext);
        restTemplate.setAccessTokenProvider(tokenProvider);
        // restTemplate.setInterceptors(Arrays.asList(ribbonInterceptor, new CompressingClientHttpRequestInterceptor()));
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
