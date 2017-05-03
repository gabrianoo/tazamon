package com.tazamon.calendar.apple;

import com.tazamon.calendar.Calendar;
import com.tazamon.calendar.CalendarRepository;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.common.DefaultDavTazamonRequest;
import com.tazamon.client.dav.xml.DisplayName;
import com.tazamon.client.dav.xml.Property;
import com.tazamon.client.dav.xml.PropertyFind;
import com.tazamon.common.ServerProperties;
import com.tazamon.common.User;
import com.tazamon.xml.XmlProcessor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class AppleCalendarRepository implements CalendarRepository {

    private final DavTazamonExecutor davTazamonExecutor;
    private final XmlProcessor xmlProcessor;
    private final DavTazamonAdapter<List<Calendar>> calendarDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public AppleCalendarRepository(
            DavTazamonExecutor davTazamonExecutor,
            XmlProcessor xmlProcessor,
            DavTazamonAdapter<List<Calendar>> calendarDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.xmlProcessor = xmlProcessor;
        this.calendarDavTazamonAdapter = calendarDavTazamonAdapter;
        this.serverProperties = serverProperties;
    }

    @Override
    public List<Calendar> findAllCalendars(User user) {
        DisplayName displayName = new DisplayName();
        Property prop = new Property(displayName);
        PropertyFind propFind = new PropertyFind(prop);
        Optional<List<Calendar>> calendars = xmlProcessor.toXml(propFind)
                .map(document -> new DefaultDavTazamonRequest(
                        user.getBase64EncodeAuthToken(),
                        document,
                        String.join(
                                "",
                                serverProperties.getCalendarServer(),
                                user.getPrincipal(),
                                "/calendars"
                        )

                ))
                .flatMap(davTazamonExecutor::execute)
                .map(calendarDavTazamonAdapter::adapt);
        return calendars.orElse(Collections.emptyList());
    }
}
