package com.gability.tazamon.client.dav.calendar;

import com.gability.tazamon.calendar.EventRepository;
import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.user.User;
import com.gability.tazamon.calendar.Event;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonExecutor;
import com.gability.tazamon.client.dav.DefaultDavTazamonRequest;
import com.gability.tazamon.configuration.ServerProperties;
import com.gability.tazamon.client.Processor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Collections;

import static net.fortuna.ical4j.model.Calendar.VCALENDAR;
import static net.fortuna.ical4j.model.Component.VEVENT;

@Named
public class DefaultEventRepository implements EventRepository {

    private static final String URL_DELIMITER = "/";
    private final DavTazamonExecutor davTazamonExecutor;
    private final Processor processor;
    private final DavTazamonAdapter<Iterable<Event>> eventDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public DefaultEventRepository(
            @Named("reportDavTazamonExecutor") DavTazamonExecutor davTazamonExecutor,
            Processor processor,
            DavTazamonAdapter<Iterable<Event>> eventDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.processor = processor;
        this.eventDavTazamonAdapter = eventDavTazamonAdapter;
        this.serverProperties = serverProperties;
    }

    @Override
    public Iterable<Event> findAllEvents(User user) {
        return processor.to(buildRequest())
                .map(document -> new DefaultDavTazamonRequest(
                        user.getBase64EncodeAuthToken(),
                        document,
                        String.join(
                                URL_DELIMITER,
                                serverProperties.getCalendarServer(),
                                user.getPrincipal(),
                                serverProperties.getCalendarRoot()
                        )
                )).flatMap(davTazamonExecutor::execute)
                .map(eventDavTazamonAdapter::adapt)
                .orElse(Collections.emptyList());
    }

    private CalendarQuery buildRequest() {
        return new CalendarQuery(
                Arrays.asList(
                        new ETag(""),
                        new CalendarData("")
                ),
                new Filter(
                        new CompFilter(
                                VCALENDAR,
                                new CompFilter(VEVENT, null)
                        )
                )
        );
    }
}
