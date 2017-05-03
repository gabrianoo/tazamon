package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.xml.*;
import com.tazamon.common.User;
import com.tazamon.exception.UserParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;

public class UserDavTazamonAdapterTest {

    private static final String OK_STATUS = "HTTP/1.1 200 OK";
    private static final String RESPONSE_HREF = "/";
    private static final String PRINCIPAL = "562457900";
    private static final String PRINCIPAL_HREF = String.join("/", PRINCIPAL, "principal");
    @Mock
    private DavTazamonRequest davTazamonRequest;
    private UserDavTazamonAdapter underTestUserDavTazamonAdapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTestUserDavTazamonAdapter = new UserDavTazamonAdapter();
        doReturn("").when(davTazamonRequest)
                .getBase64EncodeAuthToken();
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
                () -> underTestUserDavTazamonAdapter.adapt(null)
        );
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("");
    }

    @Test
    public void givenMultiStatusResponseIsEmptyWhenAdaptingUserThenEmptyUserIsReturned() {
        assertThat(
                underTestUserDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(Collections.emptyList()),
                                null
                        )
                )
        ).isNotPresent();
    }

    @Test
    public void givenMultiStatusResponseIsMoreThanOneWhenAdaptingUserThenFirstResponseIsPicked() {
        assertThat(
                underTestUserDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Arrays.asList(
                                                buildMultiStatusResponse(new CurrentUserPrincipal(PRINCIPAL_HREF)),
                                                buildMultiStatusResponse(new CurrentUserPrincipal(RESPONSE_HREF))
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        ).contains(
                User.builder().principal(PRINCIPAL).base64EncodeAuthToken("").build()
        );
    }

    @Test
    public void givenMultiStatusResponseWithInvalidPropertyTypeWhenAdaptingUserThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestUserDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new ETag(OK_STATUS))
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(UserParseException.class)
                .hasMessageContaining("DAV PropertyType must be instance of CurrentUserPrincipal");
    }

    @Test
    public void givenInvalidPrincipalWhenAdaptingUserThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestUserDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new CurrentUserPrincipal(RESPONSE_HREF))
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(UserParseException.class)
                .hasMessageContaining("Principal can't be null or missing");
    }

    @Test
    public void givenNullPrincipalWhenAdaptingUserThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestUserDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new CurrentUserPrincipal(null))
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(UserParseException.class)
                .hasMessageContaining("Principal can't be null or missing");
    }
}