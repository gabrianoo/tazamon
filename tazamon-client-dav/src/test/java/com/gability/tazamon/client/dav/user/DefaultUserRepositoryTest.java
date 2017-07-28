package com.gability.tazamon.client.dav.user;

import com.gability.tazamon.user.User;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonExecutor;
import com.gability.tazamon.client.dav.DavTazamonRequest;
import com.gability.tazamon.client.dav.DavTazamonResponse;
import com.gability.tazamon.client.dav.DefaultDavTazamonResponse;
import com.gability.tazamon.client.dav.xml.PropertyFind;
import com.gability.tazamon.configuration.ServerProperties;
import com.gability.tazamon.client.Processor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class DefaultUserRepositoryTest {

    private static final String DEFAULT_USERNAME = "ME";
    private static final String DEFAULT_PASSWORD = "SECRET";
    private static final String DEFAULT_SERVER_URL = "URL";
    private static final String DEFAULT_REQUEST_BODY = "BODY";
    @Mock
    private DavTazamonExecutor davTazamonExecutor;
    @Mock
    private Processor processor;
    @Mock
    private DavTazamonAdapter<Optional<User>> userDavTazamonAdapter;
    @Mock
    private ServerProperties serverProperties;
    private DefaultUserRepository underTestDefaultUserRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doReturn(DEFAULT_SERVER_URL).when(serverProperties)
                .getCalendarServer();
        underTestDefaultUserRepository = new DefaultUserRepository(
                davTazamonExecutor,
                processor,
                userDavTazamonAdapter,
                serverProperties
        );
    }

    @Test
    public void givenAnEmptyXmlDocumentWhenProvideUserThenAnEmptyUserIsReturned() {
        doReturn(Optional.empty()).when(processor)
                .to(any(PropertyFind.class));
        assertThat(
                underTestDefaultUserRepository.findUser(DEFAULT_USERNAME, DEFAULT_PASSWORD)
        ).isNotPresent();
    }

    @Test
    public void givenAnEmptyResponseWhenProvideUserThenAnEmptyUserIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(processor)
                .to(any(PropertyFind.class));
        doReturn(Optional.empty()).when(davTazamonExecutor)
                .execute(any(DavTazamonRequest.class));
        assertThat(
                underTestDefaultUserRepository.findUser(DEFAULT_USERNAME, DEFAULT_PASSWORD)
        ).isNotPresent();
    }

    @Test
    public void givenAnEmptyAdaptedUserWhenProvideUserThenAnEmptyUserIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(processor)
                .to(any(PropertyFind.class));
        doReturn(
                Optional.of(new DefaultDavTazamonResponse(null, null))
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Optional.empty()).when(userDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        assertThat(
                underTestDefaultUserRepository.findUser(DEFAULT_USERNAME, DEFAULT_PASSWORD)
        ).isNotPresent();
    }

    @Test
    public void givenEmailAndPasswordWhenProvideUserThenValidUserIsReturned() {
        User user = User.builder().build();
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(processor)
                .to(any(PropertyFind.class));
        doReturn(
                Optional.of(
                        new DefaultDavTazamonResponse(null, null)
                )
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Optional.of(user)).when(userDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        assertThat(
                underTestDefaultUserRepository.findUser(DEFAULT_USERNAME, DEFAULT_PASSWORD)
        ).contains(user);
    }
}