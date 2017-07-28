package com.gability.tazamon.client.dav.calendar;

import com.gability.tazamon.calendar.CalendarRepository;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DefaultDavTazamonRequest;
import com.gability.tazamon.user.User;
import com.gability.tazamon.client.Processor;
import com.gability.tazamon.calendar.Calendar;
import com.gability.tazamon.client.dav.DavTazamonExecutor;
import com.gability.tazamon.client.dav.xml.DisplayName;
import com.gability.tazamon.client.dav.xml.Property;
import com.gability.tazamon.client.dav.xml.PropertyFind;
import com.gability.tazamon.configuration.ServerProperties;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

@Named
public class DefaultCalendarRepository implements CalendarRepository {

    private static final String URL_DELIMITER = "/";
    private final DavTazamonExecutor davTazamonExecutor;
    private final Processor processor;
    private final DavTazamonAdapter<Iterable<Calendar>> calendarDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public DefaultCalendarRepository(
            @Named("propFindDavTazamonExecutor") DavTazamonExecutor davTazamonExecutor,
            Processor processor,
            DavTazamonAdapter<Iterable<Calendar>> calendarDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.processor = processor;
        this.calendarDavTazamonAdapter = calendarDavTazamonAdapter;
        this.serverProperties = serverProperties;
    }

    @Override
    public Iterable<Calendar> findAllCalendars(User user) {
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

                ))
                .flatMap(davTazamonExecutor::execute)
                .map(calendarDavTazamonAdapter::adapt)
                .orElse(Collections.emptyList());
    }

    private PropertyFind buildRequest() {
        return new PropertyFind(
                new Property(
                        new DisplayName()
                )
        );
    }
}
