package com.gability.tazamon.client.dav;

import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.client.dav.xml.Response;
import com.gability.tazamon.client.Processor;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static com.gability.tazamon.client.dav.ContentType.APPLICATION_XML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class PropFindDavTazamonExecutorTest {

    private static final String PAYLOAD = "MultiStatusUserPrincipalValidResponse.xml";
    private static final String RESPONSE_HREF = "/";
    private static final String PRINCIPAL_HREF = "/123456789/principal";
    private static final String SERVER_URL = "http://localhost";
    @Mock
    private OkHttpClient httpClient;
    @Mock
    private Processor processor;
    @Mock
    private Call call;
    private okhttp3.Response response;
    private DavTazamonExecutor underTestPropFindDavTazamonExecutor;

    @Before
    public void setup() throws ParserConfigurationException, IOException {
        MockitoAnnotations.initMocks(this);
        response = new okhttp3.Response.Builder()
                .request(
                        new Request.Builder()
                                .url(SERVER_URL)
                                .build()
                )
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("OK")
                .body(
                        ResponseBody.create(
                                MediaType.parse(APPLICATION_XML.getMediaType()),
                                PAYLOAD
                        )
                )
                .build();
        underTestPropFindDavTazamonExecutor = new PropFindDavTazamonExecutor(
                httpClient,
                processor
        );
    }

    private DavTazamonRequest buildDavTazamonRequest() {
        return new DefaultDavTazamonRequest(
                "", "", SERVER_URL
        );
    }

    private Response buildMultiStatusResponse(PropertyType propertyType) {
        return new Response(
                RESPONSE_HREF,
                new PropertyStatus(
                        new Property(
                                propertyType
                        ),
                        Status.OK.toString()
                )
        );
    }

    @Test
    public void givenNullDavTazamonResponseWhenExecutingPropFindThenNullPointerExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestPropFindDavTazamonExecutor.execute(null)
        );
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("davTazamonRequest");
    }

    @Test
    public void givenInValidHttpPropfindWhenExecutingPropFindThenIOExceptionIsThrown() throws IOException {
        doReturn(call)
                .when(httpClient).newCall(any(Request.class));
        doThrow(new IOException())
                .when(call).execute();
        assertThat(
                underTestPropFindDavTazamonExecutor.execute(
                        buildDavTazamonRequest()
                )
        ).isNotPresent();
    }

    @Test
    public void givenValidDavTazamonRequestWhenExecutingPropFindThenValidDavTazamonResponseIsReturned() throws IOException {
        doReturn(call)
                .when(httpClient).newCall(any(Request.class));
        doReturn(response)
                .when(call).execute();
        doReturn(
                Optional.of(
                        new MultiStatus(
                                Collections.singletonList(
                                        buildMultiStatusResponse(new CurrentUserPrincipal(PRINCIPAL_HREF))
                                )
                        )
                )
        ).when(processor).from(any(String.class), any());
        assertThat(
                underTestPropFindDavTazamonExecutor.execute(
                        buildDavTazamonRequest()
                )
        ).containsInstanceOf(DefaultDavTazamonResponse.class);
    }
}