package com.tazamon.client.dav.calendar;

import com.tazamon.calendar.Event;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.common.DefaultDavTazamonResponse;
import com.tazamon.client.dav.xml.CalendarQuery;
import com.tazamon.common.ServerProperties;
import com.tazamon.user.User;
import com.tazamon.xml.XmlProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class DefaultEventRepositoryTest {

    private static final String DEFAULT_BASE64TOKEN = "56789";
    private static final String DEFAULT_PRINCIPAL = "54321";
    private static final String DEFAULT_SERVER_URL = "URL";
    private static final String DEFAULT_REQUEST_BODY = "BODY";
    @Mock
    private DavTazamonExecutor davTazamonExecutor;
    @Mock
    private XmlProcessor xmlProcessor;
    @Mock
    private DavTazamonAdapter<Iterable<Event>> eventDavTazamonAdapter;
    @Mock
    private ServerProperties serverProperties;
    private DefaultEventRepository underTestDefaultEventRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doReturn(DEFAULT_SERVER_URL).when(serverProperties)
                .getCalendarServer();
        underTestDefaultEventRepository = new DefaultEventRepository(
                davTazamonExecutor,
                xmlProcessor,
                eventDavTazamonAdapter,
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
    public void givenAnEmptyXmlDocumentWhenFindAllEventsThenAnEmptyEventIterableIsReturned() {
        doReturn(Optional.empty()).when(xmlProcessor)
                .toXml(any(CalendarQuery.class));
        assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenAnEmptyResponseWhenFindAllEventsThenAnEmptyEventListIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(CalendarQuery.class));
        doReturn(Optional.empty()).when(davTazamonExecutor)
                .execute(any(DavTazamonRequest.class));
        assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenAnEmptyAdaptedEventWhenFindAllEventsThenAnEmptyEventListIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(CalendarQuery.class));
        doReturn(
                Optional.of(new DefaultDavTazamonResponse(null, null))
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Collections.emptyList()).when(eventDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenValidUserWhenFindAllEventsThenValidEventListIsReturned() {
        Event calendar = Event.builder().build();
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(xmlProcessor)
                .toXml(any(CalendarQuery.class));
        doReturn(
                Optional.of(
                        new DefaultDavTazamonResponse(null, null)
                )
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Collections.singletonList(calendar)).when(eventDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).contains(calendar);
    }
}