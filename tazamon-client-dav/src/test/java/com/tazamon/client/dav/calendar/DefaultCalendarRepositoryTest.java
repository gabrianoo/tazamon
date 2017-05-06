package com.tazamon.client.dav.calendar;

import com.tazamon.calendar.Calendar;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class DefaultCalendarRepositoryTest {

    private static final String DEFAULT_BASE64TOKEN = "56789";
    private static final String DEFAULT_PRINCIPAL = "54321";
    private static final String DEFAULT_SERVER_URL = "URL";
    private static final String DEFAULT_REQUEST_BODY = "BODY";
    @Mock
    private DavTazamonExecutor davTazamonExecutor;
    @Mock
    private XmlProcessor xmlProcessor;
    @Mock
    private DavTazamonAdapter<List<Calendar>> calendarDavTazamonAdapter;
    @Mock
    private ServerProperties serverProperties;
    private DefaultCalendarRepository underTestDefaultCalendarRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doReturn(DEFAULT_SERVER_URL).when(serverProperties)
                .getCalendarServer();
        underTestDefaultCalendarRepository = new DefaultCalendarRepository(
                davTazamonExecutor,
                xmlProcessor,
                calendarDavTazamonAdapter,
                serverProperties
        );
    }

    private User buildUser() {
        return User.builder()
                .principal(DEFAULT_PRINCIPAL)
                .base64EncodeAuthToken(DEFAULT_BASE64TOKEN)
                .build();
    }

    @Test
    public void givenAnEmptyXmlDocumentWhenFindAllCalendarsThenAnEmptyCalendarListIsReturned() {
        doReturn(Optional.empty()).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
        assertThat(
                underTestDefaultCalendarRepository.findAllCalendars(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenAnEmptyResponseWhenFindAllCalendarsThenAnEmptyCalendarListIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
        doReturn(Optional.empty()).when(davTazamonExecutor)
                .execute(any(DavTazamonRequest.class));
        assertThat(
                underTestDefaultCalendarRepository.findAllCalendars(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenAnEmptyAdaptedCalendarWhenFindAllCalendarsThenAnEmptyCalendarListIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
        doReturn(
                Optional.of(new DefaultDavTazamonResponse(null, null))
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Collections.emptyList()).when(calendarDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        assertThat(
                underTestDefaultCalendarRepository.findAllCalendars(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenValidUserWhenFindAllCalendarsThenValidCalendarListIsReturned() {
        Calendar calendar = Calendar.builder().build();
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(PropertyFind.class));
        doReturn(
                Optional.of(
                        new DefaultDavTazamonResponse(null, null)
                )
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Collections.singletonList(calendar)).when(calendarDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        assertThat(
                underTestDefaultCalendarRepository.findAllCalendars(buildUser())
        ).contains(calendar);
    }
}
