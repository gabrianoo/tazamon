package com.otasys.tazamon.web.dav;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultWebDavHttpMethodsTest {

    private HttpClient httpClient;
    private HostConfiguration hostConfiguration;
    private WebDavHttpMethods webDavHttpMethods;

    @Before
    public void setUp() {
        httpClient = mock(HttpClient.class);
        hostConfiguration = mock(HostConfiguration.class);
        webDavHttpMethods = new DefaultWebDavHttpMethods(httpClient, hostConfiguration);
    }

    @Test
    public void givenWebDavHttpMethodsWhenExecuteMethodIsCalledWithValidDavMethodBaseThenValidDavMethodBaseShouldReturn() throws IOException {
        DavMethodBase requestDavMethodBase = mock(DavMethodBase.class);
        doThrow(IOException.class).when(httpClient).executeMethod(hostConfiguration, requestDavMethodBase);
        DavMethodBase responceDavMethodBase = webDavHttpMethods.basicRequest(requestDavMethodBase);
        assertThat(responceDavMethodBase, is(equalTo(null)));
    }

    @Test
    public void givenWebDavHttpMethodsWhenExecuteMethodIsCalledWithInValidDavMethodBaseThenNullShouldReturn() throws IOException {
        DavMethodBase requestDavMethodBase = mock(DavMethodBase.class);
        webDavHttpMethods.basicRequest(requestDavMethodBase);
        verify(httpClient, times(1)).executeMethod(hostConfiguration, requestDavMethodBase);
    }
}