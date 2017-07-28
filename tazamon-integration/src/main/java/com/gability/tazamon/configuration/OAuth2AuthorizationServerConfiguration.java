package com.gability.tazamon.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

import javax.inject.Inject;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    @Inject
    public OAuth2AuthorizationServerConfiguration(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client")
                .secret("secret")
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .resourceIds("resources");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
}
