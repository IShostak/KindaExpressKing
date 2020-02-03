package com.softserve.itacademy.kek.security;

import com.softserve.itacademy.kek.controller.LogoutController;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:auth0.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    @Value(value = "${com.auth0.domain}")
    private String domain;

    /**
     * This is the client id of auth0 application
     */
    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    /**
     * This is the client secret of auth0 application
     */
    private final String clientSecret = System.getenv("clientSecret");

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutController();
    }

    @Bean
    public AuthenticationController authenticationController() {
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .withJwkProvider(jwkProvider)
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/api/v1/profile/**", "/profile/**")
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/api/v1/login")
                .successForwardUrl("/api/v1/profile")
                .and()
                .logout()
                .logoutUrl("/api/v1/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
        ;
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
