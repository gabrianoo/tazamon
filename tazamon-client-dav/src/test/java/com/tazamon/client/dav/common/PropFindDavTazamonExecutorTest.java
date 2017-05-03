package com.tazamon.client.dav.common;


import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.xml.*;
import com.tazamon.xml.XmlProcessor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class PropFindDavTazamonExecutorTest {

    private static final String XML_STRING_ENTITY = "<multistatus xmlns='DAV:'><response><href>/</href><propstat><prop><current-user-principal><href>/562457900/principal/</href></current-user-principal></prop><status>HTTP/1.1 200 OK</status></propstat></response></multistatus>";
    private static final String OK_STATUS = "HTTP/1.1 200 OK";
    private static final String RESPONSE_HREF = "/";
    private static final String PRINCIPAL_HREF = "/562457900/principal";
    @Mock
    private HttpClient httpClient;
    @Mock
    private XmlProcessor xmlProcessor;
    @Mock
    private HttpResponse httpResponse;
    private DavTazamonExecutor underTestPropFindDavTazamonExecutor;

    @Before
    public void setup() throws ParserConfigurationException {
        MockitoAnnotations.initMocks(this);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
        underTestPropFindDavTazamonExecutor = new PropFindDavTazamonExecutor(
                httpClient,
                xmlProcessor,
                documentBuilder
        );
    }

    private DavTazamonRequest buildDavTazamonRequest() {
        return new DefaultDavTazamonRequest(
                "", "", ""
        );
    }

    private Response buildMultiStatusResponse(PropertyType propertyType) {
        return new Response(
                RESPONSE_HREF,
                new PropertyStatus(
                        new Property(
                                propertyType
                        ),
                        OK_STATUS
                )
        );
    }

    @Test
    public void givenNullDavTazamonResponseWhenAdaptingUserThenNullPointerExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestPropFindDavTazamonExecutor.execute(null)
        );
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("davTazamonRequest");
    }

    @Test
    public void givenNullRequestBodyWhenAdaptingUserThenIOExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestPropFindDavTazamonExecutor.execute(
                        new DefaultDavTazamonRequest("", null, "")
                )
        );
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenInValidHttpPropfindWhenAdaptingUserThenIOExceptionIsThrown() throws IOException {
        doThrow(new IOException())
                .when(httpClient).execute(any(HttpPropfind.class));
        assertThat(
                underTestPropFindDavTazamonExecutor.execute(
                        buildDavTazamonRequest()
                )
        ).isNotPresent();
    }

    @Test
    public void givenValidDavTazamonRequestWhenAdaptingUserThenValidDavTazamonResponseIsReturned() throws IOException {
        doReturn(httpResponse)
                .when(httpClient).execute(any(HttpPropfind.class));
        doReturn(new StringEntity(XML_STRING_ENTITY))
                .when(httpResponse).getEntity();
        doReturn(
                Optional.of(
                        new MultiStatus(
                                Collections.singletonList(
                                        buildMultiStatusResponse(new CurrentUserPrincipal(PRINCIPAL_HREF))
                                )
                        )
                )
        ).when(xmlProcessor).fromXml(any(Node.class), any());
        assertThat(
                underTestPropFindDavTazamonExecutor.execute(
                        buildDavTazamonRequest()
                )
        ).containsInstanceOf(DefaultDavTazamonResponse.class);
    }
}