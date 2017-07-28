package com.gability.tazamon.client.dav.calendar;

import com.gability.tazamon.calendar.Event;
import com.gability.tazamon.client.dav.DavTazamonResponse;
import com.gability.tazamon.client.dav.Status;
import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.exception.ParsingException;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import javax.inject.Named;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Named
public class EventDavTazamonAdapter implements DavTazamonAdapter<Iterable<Event>> {

    @Override
    public Iterable<Event> adapt(@NonNull DavTazamonResponse davTazamonResponse) {
        return Optional.ofNullable(davTazamonResponse.getMultiStatus())
                .map(MultiStatus::getResponse)
                .orElse(Collections.emptyList())
                .stream().filter(this::filterResponse)
                .flatMap(this::parseResponse)
                .collect(Collectors.toList());
    }

    private boolean filterResponse(Response response) {
        return Optional.ofNullable(response)
                .map(Response::getPropstat)
                .map(PropertyStatus::getStatus)
                .filter(
                        status -> Status.OK.getStatus().equals(status)
                ).isPresent();
    }

    private Stream<Event> parseResponse(Response response) {
        String selfLink = response.getHref();
        return Optional.ofNullable(response.getPropstat().getProperty())
                .map(Property::getPropertyType)
                .map(propertyType -> parsePropertyType(propertyType, selfLink))
                .orElse(Stream.empty());
    }

    private Stream<Event> parsePropertyType(PropertyType propertyType, String selfLink) {
        if (!(propertyType instanceof CalendarData)) {
            throw new ParsingException("DAV PropertyType must be instance of CalendarData");
        }
        CalendarData calendarData = ((CalendarData) propertyType);
        return Stream.of(
                extractEvent(calendarData, selfLink)
        );
    }

    private Event extractEvent(CalendarData calendarData, String selfLink) {
        StringReader sin = new StringReader(
                Optional.ofNullable(calendarData.getValue())
                        .orElseThrow(
                                () -> new ParsingException("CalendarData value can't be null or missing")
                        )
        );
        CalendarBuilder builder = new CalendarBuilder();
        try {
            Calendar calendar = builder.build(sin);
            VEvent vEvent = (VEvent) calendar.getComponent(Component.VEVENT);
            return Event.builder()
                    .selfLink(selfLink)
                    .summary(vEvent.getSummary().getValue())
                    .build();
        } catch (IOException | ParserException e) {
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
            throw new ParsingException("Couldn't parse calendar VEVENT");
        }
    }
}
