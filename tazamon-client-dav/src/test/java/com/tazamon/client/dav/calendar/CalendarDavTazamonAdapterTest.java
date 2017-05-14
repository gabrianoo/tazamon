package com.tazamon.client.dav.calendar;

import com.tazamon.calendar.Calendar;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.common.DefaultDavTazamonResponse;
import com.tazamon.client.dav.common.Status;
import com.tazamon.client.dav.xml.*;
import com.tazamon.exception.ParsingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static com.tazamon.client.dav.common.Status.NOT_FOUND;
import static com.tazamon.client.dav.common.Status.OK;
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
        assertThat(
                underTestCalendarDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Arrays.asList(
                                                buildMultiStatusResponse(new DisplayName(CALENDAR_NAME), OK),
                                                buildMultiStatusResponse(new DisplayName(RESPONSE_HREF), NOT_FOUND)
                                        )
                                ),
                                davTazamonRequest
                        )
                )
        ).contains(
                Calendar.builder().selfLink(RESPONSE_HREF).name(CALENDAR_NAME).build()
        ).doesNotContain(
                Calendar.builder().selfLink(RESPONSE_HREF).name(RESPONSE_HREF).build()
        );
    }

    @Test
    public void givenMultiStatusResponseWithInvalidPropertyTypeWhenAdaptingCalendarThenExceptionIsThrown() {
        Throwable thrown = catchThrowable(
                () -> underTestCalendarDavTazamonAdapter.adapt(
                        new DefaultDavTazamonResponse(
                                new MultiStatus(
                                        Collections.singletonList(
                                                buildMultiStatusResponse(new ETag(OK.getStatus()), OK)
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
                                                buildMultiStatusResponse(new DisplayName(null), OK)
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
