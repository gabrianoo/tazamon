package com.tazamon.client.dav.calendar;

import com.tazamon.calendar.Event;
import com.tazamon.calendar.EventRepository;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.common.DefaultDavTazamonRequest;
import com.tazamon.client.dav.xml.*;
import com.tazamon.common.ServerProperties;
import com.tazamon.user.User;
import com.tazamon.xml.XmlProcessor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Collections;

@Named
public class DefaultEventRepository implements EventRepository {

    private static final String URL_DELIMITER = "/";
    private static final String VCALENDAR = "VCALENDAR";
    private static final String VEVENT = "VEVENT";
    private final DavTazamonExecutor davTazamonExecutor;
    private final XmlProcessor xmlProcessor;
    private final DavTazamonAdapter<Iterable<Event>> eventDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public DefaultEventRepository(
            @Named("reportDavTazamonExecutor") DavTazamonExecutor davTazamonExecutor,
            XmlProcessor xmlProcessor,
            DavTazamonAdapter<Iterable<Event>> eventDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.xmlProcessor = xmlProcessor;
        this.eventDavTazamonAdapter = eventDavTazamonAdapter;
        this.serverProperties = serverProperties;
    }

    @Override
    public Iterable<Event> findAllEvents(User user) {
        return xmlProcessor.toXml(buildRequest())
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
                        new ETag(null),
                        new CalendarData(null)
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
