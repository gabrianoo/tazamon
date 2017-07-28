package com.gability.tazamon.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.inject.Inject;

import static java.lang.Boolean.TRUE;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppleSecurityProvider appleSecurityProvider;

    @Inject
    public WebSecurityConfiguration(AppleSecurityProvider appleSecurityProvider) {
        super(TRUE);
        this.appleSecurityProvider = appleSecurityProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(appleSecurityProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}
