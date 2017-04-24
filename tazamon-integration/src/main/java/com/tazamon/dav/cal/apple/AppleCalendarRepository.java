package com.tazamon.dav.cal.apple;

import com.tazamon.dav.cal.Calendar;
import com.tazamon.dav.cal.CalendarRepository;
import com.tazamon.dav.common.FreeMarkerContentProducer;
import com.tazamon.dav.configuration.AppleWebDavProperties;
import com.tazamon.dav.web.DavAdapter;
import com.tazamon.dav.web.DavRequest;
import com.tazamon.dav.web.User;
import com.tazamon.dav.web.WebDavRequest;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class AppleCalendarRepository implements CalendarRepository {

    private final WebDavRequest webDavRequest;
    private final FreeMarkerContentProducer freeMarkerContentProducer;
    private final DavAdapter<List<Calendar>> listCalendarDavAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleCalendarRepository(
            WebDavRequest webDavRequest,
            FreeMarkerContentProducer freeMarkerContentProducer,
            DavAdapter<List<Calendar>> listCalendarDavAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.webDavRequest = webDavRequest;
        this.freeMarkerContentProducer = freeMarkerContentProducer;
        this.listCalendarDavAdapter = listCalendarDavAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    @Override
    public Optional<List<Calendar>> findAllCalendars(User user) {
        return freeMarkerContentProducer
                .processTemplateIntoOptionalString(appleWebDavProperties.getFtl().getDisplayName(), null)
                .flatMap(document -> Optional.of(
                        DavRequest.builder()
                                .base64EncodeAuthToken(user.getBase64EncodeAuthToken())
                                .httpEntity(document)
                                .serverUrl(appleWebDavProperties.getCardServer())
                                .build()
                ))
                .flatMap(webDavRequest::submitRequest)
                .flatMap(listCalendarDavAdapter::adapt);
    }
}
