package com.otasys.tazamon.web.dav.configuration;

import org.apache.commons.httpclient.HttpClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class HttpClientConfigurationTest {

    private HttpClientConfiguration httpClientConfiguration;

    @Before
    public void setupTest() {
        httpClientConfiguration = new HttpClientConfiguration();
    }

    @Test
    public void givenHttpClientConfigurationWhenProvideHttpClientThenHttpClientShouldReturn() {
        assertThat(
                httpClientConfiguration.provideHttpClient(),
                is(instanceOf(HttpClient.class))
        );
    }
}