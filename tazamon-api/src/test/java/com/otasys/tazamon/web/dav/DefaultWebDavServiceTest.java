package com.otasys.tazamon.web.dav;

import com.otasys.tazamon.template.FreeMarkerTemplateService;
import com.otasys.tazamon.xml.XmlConversionService;
import freemarker.template.Configuration;
import org.apache.commons.httpclient.Header;
import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DefaultWebDavServiceTest {

    private Configuration freemarkerConfiguration;
    private XmlConversionService xmlConversionService;
    private FreeMarkerTemplateService freeMarkerTemplateService;
    private WebDavHttpMethods webDavHttpMethods;
    private WebDavService webDavService;

    @Before
    public void setUp() {
        freemarkerConfiguration = mock(Configuration.class);
        xmlConversionService = mock(XmlConversionService.class);
        webDavHttpMethods = mock(WebDavHttpMethods.class);
        freeMarkerTemplateService = mock(FreeMarkerTemplateService.class);
        webDavService = new DefaultWebDavService(
                freemarkerConfiguration,
                xmlConversionService,
                freeMarkerTemplateService,
                webDavHttpMethods
        );
    }

    @Test
    public void givenWebDavServiceAndValidWedDavServerWhenIsWebDavSupportedThenTrueShouldReturn() throws IOException {
        DavMethodBase result = mock(DavMethodBase.class);
        doReturn(result).when(webDavHttpMethods).requestWithHeaders(any(DavMethodBase.class));
        Header allowHeader = mock(Header.class);
        doReturn(allowHeader).when(result).getResponseHeader("Allow");
        doReturn("REPORT PROPFIND PROPPATCH").when(allowHeader).getValue();
        assertThat(
                webDavService.isWebDavSupported(),
                is(equalTo(true))
        );
    }
}