package com.tazamon.calendar.apple;

import com.tazamon.calendar.Calendar;
import com.tazamon.calendar.CalendarRepository;
import com.tazamon.client.dav.DefaultDavTazamonRequest;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.common.FreeMarkerContentProducer;
import com.tazamon.common.User;
import com.tazamon.configuration.AppleWebDavProperties;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class AppleCalendarRepository implements CalendarRepository {

    private final DavTazamonExecutor davTazamonExecutor;
    private final FreeMarkerContentProducer freeMarkerContentProducer;
    private final DavTazamonAdapter<List<Calendar>> calendarDavTazamonAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleCalendarRepository(
            DavTazamonExecutor davTazamonExecutor,
            FreeMarkerContentProducer freeMarkerContentProducer,
            DavTazamonAdapter<List<Calendar>> calendarDavTazamonAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.freeMarkerContentProducer = freeMarkerContentProducer;
        this.calendarDavTazamonAdapter = calendarDavTazamonAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    @Override
    public List<Calendar> findAllCalendars(User user) {
        Optional<List<Calendar>> calendars = freeMarkerContentProducer
                .processTemplateIntoOptionalString(appleWebDavProperties.getFtl().getDisplayName(), null)
                .map(document -> new DefaultDavTazamonRequest(
                        user.getBase64EncodeAuthToken(),
                        document,
                        String.join(
                                "",
                                appleWebDavProperties.getCalendarServer(),
                                user.getPrincipal(),
                                "/calendars"
                        )

                ))
                .flatMap(davTazamonExecutor::execute)
                .map(calendarDavTazamonAdapter::adapt);
        return calendars.orElse(Collections.emptyList());
    }
}
