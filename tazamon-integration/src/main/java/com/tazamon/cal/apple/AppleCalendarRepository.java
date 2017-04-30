package com.tazamon.cal.apple;

import com.tazamon.calendar.Calendar;
import com.tazamon.calendar.CalendarRepository;
import com.tazamon.client.http.DefaultHttpTazamonRequest;
import com.tazamon.client.http.HttpTazamonAdapter;
import com.tazamon.client.http.HttpTazamonRequester;
import com.tazamon.common.FreeMarkerContentProducer;
import com.tazamon.common.User;
import com.tazamon.configuration.AppleWebDavProperties;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class AppleCalendarRepository implements CalendarRepository {

    private final HttpTazamonRequester httpTazamonRequester;
    private final FreeMarkerContentProducer freeMarkerContentProducer;
    private final HttpTazamonAdapter<List<Calendar>, DavPropertySet> calendarHttpTazamonAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleCalendarRepository(
            HttpTazamonRequester httpTazamonRequester,
            FreeMarkerContentProducer freeMarkerContentProducer,
            HttpTazamonAdapter<List<Calendar>, DavPropertySet> calendarHttpTazamonAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.httpTazamonRequester = httpTazamonRequester;
        this.freeMarkerContentProducer = freeMarkerContentProducer;
        this.calendarHttpTazamonAdapter = calendarHttpTazamonAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    @Override
    public List<Calendar> findAllCalendars(User user) {
        Optional<List<Calendar>> calendars = freeMarkerContentProducer
                .processTemplateIntoOptionalString(appleWebDavProperties.getFtl().getDisplayName(), null)
                .map(document -> new DefaultHttpTazamonRequest(
                        user.getBase64EncodeAuthToken(),
                        document,
                        appleWebDavProperties.getCardServer()

                ))
                .flatMap(httpTazamonRequester::submitRequest)
                .map(calendarHttpTazamonAdapter::adapt);
        return calendars.orElse(Collections.emptyList());
    }
}
