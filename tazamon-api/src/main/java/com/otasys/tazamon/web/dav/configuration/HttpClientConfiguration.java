package com.otasys.tazamon.web.dav.configuration;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public HttpClient provideHttpClient() {
        return new HttpClient();
    }
}
