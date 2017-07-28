package com.gability.tazamon.client.dav.user;

import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonRequest;
import com.gability.tazamon.client.dav.DefaultDavTazamonResponse;
import com.gability.tazamon.client.dav.Status;
import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.exception.ParsingException;
import com.gability.tazamon.user.User;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;

public class UserDavTazamonAdapterTest {

    private static final String RESPONSE_HREF = "/";
    private static final String PRINCIPAL = "562457900";
    private static final String PRINCIPAL_HREF = String.join("/", PRINCIPAL, "principal");
    @Mock
    private DavTazamonRequest davTazamonRequest;
    private DavTazamonAdapter<Optional<User>> underTestUserDavTazamonAdapter;

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
                        Status.OK.getStatus()
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
                .hasMessageContaining("davTazamonResponse");
    }

    @Test
    public void givenMultiStatusResponseIsEmptyWhenAdaptingUserThenEmptyUserIsReturned() {
        Assertions.assertThat(
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
        Assertions.assertThat(
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
                User.builder().selfLink(RESPONSE_HREF).principal(PRINCIPAL).base64EncodeAuthToken("").build()
        );
    }

    @Test
    public void givenMultiStatusResponseWithInvalidPropertyTypeWhenAdaptingUserThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestUserDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new ETag(Status.OK.getStatus()))
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(ParsingException.class)
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
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("Principal can't be null or missing");
    }

    @Test
    public void givenInvalidBase64TokenWhenAdaptingUserThenExceptionIsThrown() {
        doReturn(null).when(davTazamonRequest)
                .getBase64EncodeAuthToken();
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
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("Base64Encoded Token can't be null or missing");
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
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("Principal can't be null or missing");
    }
}