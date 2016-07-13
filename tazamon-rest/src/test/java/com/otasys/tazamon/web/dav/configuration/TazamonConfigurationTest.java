package com.otasys.tazamon.web.dav.configuration;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl;
import org.apache.commons.httpclient.HttpClient;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class TazamonConfigurationTest {

    private TazamonConfiguration tazamonConfiguration;

    @Before
    public void setupTest() {
        tazamonConfiguration = new TazamonConfiguration();
    }

    @Test
    public void givenHttpClientConfigurationWhenProvideHttpClientThenHttpClientShouldReturn() {
        assertThat(
                tazamonConfiguration.provideHttpClient(),
                is(instanceOf(HttpClient.class))
        );
    }

    @Test
    public void givenW3cXmlConfigurationWhenProvideTransformerFactoryThenTransformerFactoryImplShouldReturn() {
        assertThat(
                tazamonConfiguration.provideTransformerFactory(),
                is(instanceOf(TransformerFactoryImpl.class))
        );
    }

    @Test
    public void givenW3cXmlConfigurationWhenProvideTransformerAndValidTransformerFactoryThenTransformerShouldReturn() throws TransformerConfigurationException {
        assertThat(
                tazamonConfiguration.provideTransformer(tazamonConfiguration.provideTransformerFactory()),
                is(instanceOf(TransformerImpl.class))
        );
    }

    @Test(expected = TransformerConfigurationException.class)
    public void givenW3cXmlConfigurationWhenProvideTransformerAndInValidTransformerFactoryThenTransformerShouldReturn() throws TransformerConfigurationException {
        TransformerFactory mockTransformerFactory = mock(TransformerFactory.class);
        doThrow(TransformerConfigurationException.class).when(mockTransformerFactory).newTransformer();
        tazamonConfiguration.provideTransformer(mockTransformerFactory);
    }

}