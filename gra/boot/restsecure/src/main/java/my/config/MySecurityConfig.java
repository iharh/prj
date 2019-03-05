package my.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.Arrays;
import java.util.Collection;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    public static class NlpServiceAuthenticationToken extends AbstractAuthenticationToken {
        private static final long serialVersionUID = -1949976839306453197L;
            
        public NlpServiceAuthenticationToken() {
            super(Arrays.asList());
            setAuthenticated(true);
        }
        
        public NlpServiceAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(myFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .httpBasic()
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

    private Filter myFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                    throws IOException, ServletException {

                logger.info("!!! myFilter enter !!!");

                Authentication authResult = new NlpServiceAuthenticationToken();

                SecurityContextHolder.getContext().setAuthentication(authResult);
                
                chain.doFilter(request, response);
            }
        };
    }
}
