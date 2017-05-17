package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.xml.*;
import com.tazamon.xml.XmlProcessor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.InputStreamEntity;
import org.apache.jackrabbit.webdav.client.methods.HttpReport;
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

import static com.tazamon.client.dav.common.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class ReportDavTazamonExecutorTest {

    private static final String PAYLOAD = "MultiStatusCalendarDataValidResponse.xml";
    private static final String V_EVENT_STRING = "VEVENT";
    private static final String RESPONSE_HREF = "/";
    @Mock
    private HttpClient httpClient;
    @Mock
    private XmlProcessor xmlProcessor;
    @Mock
    private HttpResponse httpResponse;
    private DavTazamonExecutor underTestReportDavTazamonExecutor;

    @Before
    public void setup() throws ParserConfigurationException {
        MockitoAnnotations.initMocks(this);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
        underTestReportDavTazamonExecutor = new ReportDavTazamonExecutor(
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
                        OK.toString()
                )
        );
    }

    @Test
    public void givenNullDavTazamonResponseWhenExecutingReportThenNullPointerExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestReportDavTazamonExecutor.execute(null)
        );
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("davTazamonRequest");
    }

    @Test
    public void givenInValidHttpReportWhenExecutingReportThenIOExceptionIsThrown() throws IOException {
        doThrow(new IOException())
                .when(httpClient).execute(any(HttpReport.class));
        assertThat(
                underTestReportDavTazamonExecutor.execute(
                        buildDavTazamonRequest()
                )
        ).isNotPresent();
    }

    @Test
    public void givenValidDavTazamonRequestWhenExecutingReportThenValidDavTazamonResponseIsReturned() throws IOException {
        doReturn(httpResponse)
                .when(httpClient).execute(any(HttpReport.class));
        doReturn(new InputStreamEntity(getClass().getResourceAsStream(PAYLOAD)))
                .when(httpResponse).getEntity();
        doReturn(
                Optional.of(
                        new MultiStatus(
                                Collections.singletonList(
                                        buildMultiStatusResponse(new CalendarData(V_EVENT_STRING))
                                )
                        )
                )
        ).when(xmlProcessor).fromXml(any(Node.class), any());
        assertThat(
                underTestReportDavTazamonExecutor.execute(
                        buildDavTazamonRequest()
                )
        ).containsInstanceOf(DefaultDavTazamonResponse.class);
    }
}
