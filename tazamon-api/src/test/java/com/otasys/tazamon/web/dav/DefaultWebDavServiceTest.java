package com.otasys.tazamon.web.dav;

import com.otasys.tazamon.template.FreeMarkerTemplateService;
import com.otasys.tazamon.xml.XmlConversionService;
import freemarker.template.Configuration;
import org.apache.commons.httpclient.Header;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultWebDavServiceTest {

    private Configuration freemarkerConfiguration;
    private XmlConversionService xmlConversionService;
    private FreeMarkerTemplateService freeMarkerTemplateService;
    private WebDavHttpMethods webDavHttpMethods;
    private WebDavService webDavService;
    private Unmarshaller unmarshaller;

    @Before
    public void setUp() {
        freemarkerConfiguration = mock(Configuration.class);
        xmlConversionService = mock(XmlConversionService.class);
        webDavHttpMethods = mock(WebDavHttpMethods.class);
        freeMarkerTemplateService = mock(FreeMarkerTemplateService.class);
        unmarshaller = mock(Unmarshaller.class);
        webDavService = new DefaultWebDavService(
                freemarkerConfiguration,
                xmlConversionService,
                freeMarkerTemplateService,
                webDavHttpMethods,
                unmarshaller
        );
    }

    @Test
    public void givenWebDavServiceWhenIsWebDavSupportedThenTrueShouldReturn() throws IOException {
        DavMethodBase result = mock(DavMethodBase.class);
        doReturn(result).when(webDavHttpMethods).basicRequest(any(DavMethodBase.class));
        Header allowHeader = mock(Header.class);
        doReturn(allowHeader).when(result).getResponseHeader("Allow");
        doReturn("REPORT PROPFIND PROPPATCH").when(allowHeader).getValue();
        assertThat(
                webDavService.isWebDavSupported(),
                is(equalTo(true))
        );
    }

    @Test
    public void givenWebDavServiceWhenErrorOccurWhileIsWebDavSupportedThenFalseShouldReturn() throws IOException {
        doReturn(null).when(webDavHttpMethods).basicRequest(any(DavMethodBase.class));
        assertThat(
                webDavService.isWebDavSupported(),
                is(equalTo(false))
        );
    }

    @Test
    public void givenWebDavServiceWhenGetUserPrincipalUnifiedIdAndInvalidResponseThenEmptyPrincipalShouldReturn() throws IOException, DavException {
        DavMethodBase resultDavMethodBase = mock(DavMethodBase.class);
        MultiStatus resultMultiStatus = mock(MultiStatus.class);
        doReturn(resultMultiStatus).when(resultDavMethodBase).getResponseBodyAsMultiStatus();
        doReturn(new MultiStatusResponse[0]).when(resultMultiStatus).getResponses();
        doReturn(resultDavMethodBase).when(webDavHttpMethods).basicRequest(any(DavMethodBase.class));
        assertThat(
                webDavService.getUserPrincipalUnifiedId(""),
                is(equalTo(""))
        );
    }

    @Test
    public void givenWebDavServiceWhenGetUserPrincipalUnifiedIdAndExceptionIsThrownThenEmptyPrincipalShouldReturn() throws IOException, DavException {
        doThrow(IOException.class).when(webDavHttpMethods).basicRequest(any(DavMethodBase.class));
        assertThat(
                webDavService.getUserPrincipalUnifiedId(""),
                is(equalTo(""))
        );
    }

    @Test
    public void givenWebDavServiceWhenGetUserPrincipalUnifiedIdAndEmptySuccessDavPropertiesThenEmptyPrincipalShouldReturn() throws IOException, DavException {
        DavMethodBase resultDavMethodBase = mock(DavMethodBase.class);
        MultiStatus resultMultiStatus = mock(MultiStatus.class);
        MultiStatusResponse resultMultiStatusResponse = mock(MultiStatusResponse.class);
        doReturn(resultMultiStatus).when(resultDavMethodBase).getResponseBodyAsMultiStatus();
        doReturn(new MultiStatusResponse[]{resultMultiStatusResponse}).when(resultMultiStatus).getResponses();
        DavPropertySet resultDavPropertySet = mock(DavPropertySet.class);
        doReturn(resultDavPropertySet).when(resultMultiStatusResponse).getProperties(200);
        doReturn(true).when(resultDavPropertySet).isEmpty();
        doReturn(resultDavMethodBase).when(webDavHttpMethods).basicRequest(any(DavMethodBase.class));
        assertThat(
                webDavService.getUserPrincipalUnifiedId(""),
                is(equalTo(""))
        );
    }

    @Test
    public void givenWebDavServiceWhenGetUserPrincipalUnifiedIdThenValidPrincipalShouldReturn() throws IOException, DavException, JAXBException {
        DavMethodBase resultDavMethodBase = mock(DavMethodBase.class);
        MultiStatus resultMultiStatus = mock(MultiStatus.class);
        MultiStatusResponse resultMultiStatusResponse = mock(MultiStatusResponse.class);
        doReturn(resultMultiStatus).when(resultDavMethodBase).getResponseBodyAsMultiStatus();
        doReturn(new MultiStatusResponse[]{resultMultiStatusResponse}).when(resultMultiStatus).getResponses();
        DavPropertySet resultDavPropertySet = mock(DavPropertySet.class);
        doReturn(resultDavPropertySet).when(resultMultiStatusResponse).getProperties(200);
        DavProperty davProperty = mock(DavProperty.class);
        doReturn(davProperty).when(resultDavPropertySet).get("current-user-principal");
        doReturn(resultDavMethodBase).when(webDavHttpMethods).basicRequest(any(DavMethodBase.class));
        JAXBElement jaxbElement = mock(JAXBElement.class);
        doReturn(jaxbElement).when(unmarshaller).unmarshal(any(Node.class), any(Class.class));
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setHref("/123456/principal");
        doReturn(userPrincipal).when(jaxbElement).getValue();
        assertThat(
                webDavService.getUserPrincipalUnifiedId("BASIC_AUTH"),
                is(equalTo("123456"))
        );

//                JAXBElement<UserPrincipal> userPrincipalJAXBElement = unmarshaller.unmarshal(currentUserPrincipalProperty.toXml(doc), UserPrincipal.class);
//                UserPrincipal userPrincipal = userPrincipalJAXBElement.getValue();
//                principal = userPrincipal.getPrincipal();


    }
}