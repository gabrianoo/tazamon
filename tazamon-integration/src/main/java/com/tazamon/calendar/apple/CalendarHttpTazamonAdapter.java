package com.tazamon.calendar.apple;

import com.tazamon.calendar.Calendar;
import com.tazamon.client.http.DisplayName;
import com.tazamon.client.http.HttpTazamonAdapter;
import com.tazamon.client.http.HttpTazamonResponse;
import com.tazamon.client.http.PropertyType;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class CalendarHttpTazamonAdapter implements HttpTazamonAdapter<List<Calendar>> {

    private static final String STATUS_OK = "200 OK";

    @Override
    public List<Calendar> adapt(HttpTazamonResponse httpTazamonResponse) {
        return httpTazamonResponse.getMultiStatus().getResponse()
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
