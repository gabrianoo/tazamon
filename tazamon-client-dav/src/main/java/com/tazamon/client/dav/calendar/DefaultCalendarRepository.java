package com.tazamon.client.dav.calendar;

import com.tazamon.calendar.Calendar;
import com.tazamon.calendar.CalendarRepository;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.common.DefaultDavTazamonRequest;
import com.tazamon.client.dav.xml.DisplayName;
import com.tazamon.client.dav.xml.Property;
import com.tazamon.client.dav.xml.PropertyFind;
import com.tazamon.common.ServerProperties;
import com.tazamon.user.User;
import com.tazamon.xml.XmlProcessor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

@Named
public class DefaultCalendarRepository implements CalendarRepository {

    private static final String URL_DELIMITER = "/";
    private final DavTazamonExecutor davTazamonExecutor;
    private final XmlProcessor xmlProcessor;
    private final DavTazamonAdapter<Iterable<Calendar>> calendarDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public DefaultCalendarRepository(
            @Named("propFindDavTazamonExecutor") DavTazamonExecutor davTazamonExecutor,
            XmlProcessor xmlProcessor,
            DavTazamonAdapter<Iterable<Calendar>> calendarDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.xmlProcessor = xmlProcessor;
        this.calendarDavTazamonAdapter = calendarDavTazamonAdapter;
        this.serverProperties = serverProperties;
    }

    @Override
    public Iterable<Calendar> findAllCalendars(User user) {
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
