package com.tazamon.client.dav.user;

import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.common.DefaultDavTazamonResponse;
import com.tazamon.client.dav.xml.PropertyFind;
import com.tazamon.common.ServerProperties;
import com.tazamon.user.User;
import com.tazamon.xml.XmlProcessor;
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
    private XmlProcessor xmlProcessor;
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
                xmlProcessor,
                userDavTazamonAdapter,
                serverProperties
        );
    }

    @Test
    public void givenAnEmptyXmlDocumentWhenProvideUserThenAnEmptyUserIsReturned() {
        doReturn(Optional.empty()).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
        assertThat(
                underTestDefaultUserRepository.findUser(DEFAULT_USERNAME, DEFAULT_PASSWORD)
        ).isNotPresent();
    }

    @Test
    public void givenAnEmptyResponseWhenProvideUserThenAnEmptyUserIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
        doReturn(Optional.empty()).when(davTazamonExecutor)
                .execute(any(DavTazamonRequest.class));
        assertThat(
                underTestDefaultUserRepository.findUser(DEFAULT_USERNAME, DEFAULT_PASSWORD)
        ).isNotPresent();
    }

    @Test
    public void givenAnEmptyAdaptedUserWhenProvideUserThenAnEmptyUserIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
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
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
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