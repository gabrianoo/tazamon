package com.gability.tazamon.client.dav.calendar;

import com.gability.tazamon.client.dav.Status;
import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.calendar.Event;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonRequest;
import com.gability.tazamon.client.dav.DefaultDavTazamonResponse;
import com.gability.tazamon.exception.ParsingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;

public class EventDavTazamonAdapterTest {

    private static final String PAYLOAD = "IcsValidCalendar.ics";
    private static final String EVENT_SUMMARY = "Soda DeWarming";
    private static final String RESPONSE_HREF = "/";
    @Mock
    private DavTazamonRequest davTazamonRequest;
    private String icsPayload;
    private DavTazamonAdapter<Iterable<Event>> underTestEventDavTazamonAdapter;

    @Before
    public void setup() throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream(PAYLOAD);
        byte[] resourceAsBytes = new byte[resourceAsStream.available()];
        int read = resourceAsStream.read(resourceAsBytes);
        assertThat(read).isGreaterThan(0);
        resourceAsStream.close();
        icsPayload = new String(resourceAsBytes);
        MockitoAnnotations.initMocks(this);
        underTestEventDavTazamonAdapter = new EventDavTazamonAdapter();
        doReturn("").when(davTazamonRequest)
                .getBase64EncodeAuthToken();
    }

    private Response buildMultiStatusResponse(PropertyType propertyType, Status status) {
        return new Response(
                RESPONSE_HREF,
                new PropertyStatus(
                        new Property(
                                propertyType
                        ),
                        status.getStatus()
                )
        );
    }

    @Test
    public void givenNullDavTazamonResponseWhenAdaptingEventThenNullPointerExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestEventDavTazamonAdapter.adapt(null)
        );
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("davTazamonResponse");
    }

    @Test
    public void givenMultiStatusResponseIsEmptyWhenAdaptingEventThenEmptyEventListIsReturned() {
        assertThat(
                underTestEventDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(Collections.emptyList()),
                                null
                        )
                )
        ).isEmpty();
    }

    @Test
    public void givenMultiStatusResponseIsMoreThanOneWhenAdaptingEventThenOnlyOkStatusResponseIsPicked() {
        assertThat(
                underTestEventDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Arrays.asList(
                                                buildMultiStatusResponse(new CalendarData(icsPayload), Status.OK),
                                                buildMultiStatusResponse(new CalendarData(RESPONSE_HREF), Status.NOT_FOUND)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        ).contains(
                Event.builder().selfLink(RESPONSE_HREF).summary(EVENT_SUMMARY).build()
        ).doesNotContain(
                Event.builder().selfLink(RESPONSE_HREF).summary(RESPONSE_HREF).build()
        );
    }

    @Test
    public void givenMultiStatusResponseWithInvalidPropertyTypeWhenAdaptingEventThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestEventDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new ETag(Status.OK.getStatus()), Status.OK)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("DAV PropertyType must be instance of CalendarData");
    }

    @Test
    public void givenNullCalendarDataWhenAdaptingEventThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestEventDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new CalendarData(null), Status.OK)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("CalendarData value can't be null or missing");
    }

    @Test
    public void givenInValidCalendarDataWhenAdaptingEventThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestEventDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new CalendarData(PAYLOAD), Status.OK)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("Couldn't parse calendar VEVENT");
    }
}