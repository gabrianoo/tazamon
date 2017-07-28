package com.gability.tazamon.client.dav.calendar;

import com.gability.tazamon.calendar.Event;
import com.gability.tazamon.client.dav.DavTazamonRequest;
import com.gability.tazamon.client.dav.DavTazamonResponse;
import com.gability.tazamon.client.dav.xml.CalendarQuery;
import com.gability.tazamon.user.User;
import com.gability.tazamon.client.Processor;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonExecutor;
import com.gability.tazamon.client.dav.DefaultDavTazamonResponse;
import com.gability.tazamon.configuration.ServerProperties;
import org.assertj.core.api.Assertions;
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
    private Processor processor;
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
                processor,
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
        doReturn(Optional.empty()).when(processor)
                .to(any(CalendarQuery.class));
        Assertions.assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenAnEmptyResponseWhenFindAllEventsThenAnEmptyEventListIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(processor)
                .to(any(CalendarQuery.class));
        doReturn(Optional.empty()).when(davTazamonExecutor)
                .execute(any(DavTazamonRequest.class));
        Assertions.assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenAnEmptyAdaptedEventWhenFindAllEventsThenAnEmptyEventListIsReturned() {
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(processor)
                .to(any(CalendarQuery.class));
        doReturn(
                Optional.of(new DefaultDavTazamonResponse(null, null))
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Collections.emptyList()).when(eventDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        Assertions.assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).isEmpty();
    }

    @Test
    public void givenValidUserWhenFindAllEventsThenValidEventListIsReturned() {
        Event calendar = Event.builder().build();
        doReturn(Optional.of(DEFAULT_REQUEST_BODY)).when(processor)
                .to(any(CalendarQuery.class));
        doReturn(
                Optional.of(
                        new DefaultDavTazamonResponse(null, null)
                )
        ).when(davTazamonExecutor).execute(any(DavTazamonRequest.class));
        doReturn(Collections.singletonList(calendar)).when(eventDavTazamonAdapter)
                .adapt(any(DavTazamonResponse.class));
        Assertions.assertThat(
                underTestDefaultEventRepository.findAllEvents(buildUser())
        ).contains(calendar);
    }
}