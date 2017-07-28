package com.gability.tazamon.client.dav.calendar;

import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonRequest;
import com.gability.tazamon.client.dav.DefaultDavTazamonResponse;
import com.gability.tazamon.client.dav.Status;
import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.exception.ParsingException;
import com.gability.tazamon.calendar.Calendar;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;

public class CalendarDavTazamonAdapterTest {

    private static final String RESPONSE_HREF = "/";
    private static final String CALENDAR_NAME = "Work";
    @Mock
    private DavTazamonRequest davTazamonRequest;
    private DavTazamonAdapter<Iterable<Calendar>> underTestCalendarDavTazamonAdapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTestCalendarDavTazamonAdapter = new CalendarDavTazamonAdapter();
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
    public void givenNullDavTazamonResponseWhenAdaptingCalendarThenNullPointerExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestCalendarDavTazamonAdapter.adapt(null)
        );
        assertThat(thrown)
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("davTazamonResponse");
    }

    @Test
    public void givenMultiStatusResponseIsEmptyWhenAdaptingCalendarThenEmptyCalendarIterableIsReturned() {
        assertThat(
                underTestCalendarDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(Collections.emptyList()),
                                null
                        )
                )
        ).isEmpty();
    }

    @Test
    public void givenMultiStatusResponseIsMoreThanOneWhenAdaptingCalendarThenOnlyOkStatusResponseIsPicked() {
        Calendar expectedCalendar = Calendar.builder().selfLink(RESPONSE_HREF).name(CALENDAR_NAME).build();
        assertThat(
                underTestCalendarDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Arrays.asList(
                                                buildMultiStatusResponse(new DisplayName(CALENDAR_NAME), Status.OK),
                                                buildMultiStatusResponse(new DisplayName(RESPONSE_HREF), Status.NOT_FOUND)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        ).contains(
                expectedCalendar
        ).doesNotContain(
                Calendar.builder().selfLink(RESPONSE_HREF).name(RESPONSE_HREF).build()
        ).describedAs(
                expectedCalendar.toString()
        );
    }

    @Test
    public void givenMultiStatusResponseWithInvalidPropertyTypeWhenAdaptingCalendarThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestCalendarDavTazamonAdapter.adapt(
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
                .hasMessageContaining("DAV PropertyType must be instance of DisplayName");
    }

    @Test
    public void givenNullDisplayNameWhenAdaptingCalendarThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestCalendarDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new DisplayName(null), Status.OK)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        );
        assertThat(thrown)
                .isInstanceOf(ParsingException.class)
                .hasMessageContaining("DisplayName value can't be null or missing");
    }
}
