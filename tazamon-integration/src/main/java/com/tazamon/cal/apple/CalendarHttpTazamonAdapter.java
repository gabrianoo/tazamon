package com.tazamon.cal.apple;

import com.tazamon.calendar.Calendar;
import com.tazamon.client.http.HttpTazamonAdapter;
import com.tazamon.client.http.HttpTazamonResponse;
import com.tazamon.client.http.HttpTazamonResponsePropertyLookUp;
import com.tazamon.common.XmlProcessor;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Named
public class CalendarHttpTazamonAdapter implements HttpTazamonAdapter<List<Calendar>, DavPropertySet> {

    private static final String DISPLAY_NAME_DAV_PROP_NAME = "displayname";
    private final XmlProcessor xmlProcessor;
    private final HttpTazamonResponsePropertyLookUp<DavPropertySet> httpTazamonResponsePropertyLookUp;

    @Inject
    public CalendarHttpTazamonAdapter(
            XmlProcessor xmlProcessor,
            HttpTazamonResponsePropertyLookUp<DavPropertySet> httpTazamonResponsePropertyLookUp
    ) {
        this.xmlProcessor = xmlProcessor;
        this.httpTazamonResponsePropertyLookUp = httpTazamonResponsePropertyLookUp;
    }

    @Override
    public List<Calendar> adapt(HttpTazamonResponse<DavPropertySet> httpTazamonResponse) {
        return httpTazamonResponsePropertyLookUp.lookUpProperty(httpTazamonResponse, DISPLAY_NAME_DAV_PROP_NAME)
                .flatMap(element -> xmlProcessor.fromXml(element, Calendar.class))
                .map(
                        (Function<Calendar, List<Calendar>>) calendar -> Collections.emptyList()
                )
                .orElse(Collections.emptyList());
    }
}
