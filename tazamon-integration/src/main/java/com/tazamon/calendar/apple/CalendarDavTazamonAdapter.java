package com.tazamon.calendar.apple;

import com.tazamon.calendar.Calendar;
import com.tazamon.client.dav.xml.DisplayName;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.PropertyType;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class CalendarDavTazamonAdapter implements DavTazamonAdapter<List<Calendar>> {

    private static final String STATUS_OK = "200 OK";

    @Override
    public List<Calendar> adapt(DavTazamonResponse davTazamonResponse) {
        return davTazamonResponse.getMultiStatus().getResponse()
                .stream().filter(
                        response -> response.getPropstat().getStatus().contains(STATUS_OK)
                ).map(response -> {
                            PropertyType propertyType = response.getPropstat().getProperty().getPropertyType();
                            if (propertyType instanceof DisplayName) {
                                DisplayName displayName = ((DisplayName) propertyType);
                                return Calendar.builder()
                                        .name(displayName.getValue())
                                        .build();
                            }
                            return Calendar.builder().build();
                        }
                ).collect(Collectors.toList());
    }
}
