package com.tazamon.client.dav.calendar;

import com.tazamon.calendar.Calendar;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.*;
import com.tazamon.exception.UserParseException;
import lombok.NonNull;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tazamon.client.dav.common.Status.OK;

@Named
public class CalendarDavTazamonAdapter implements DavTazamonAdapter<List<Calendar>> {

    @Override
    public List<Calendar> adapt(@NonNull DavTazamonResponse davTazamonResponse) {
        return Optional.ofNullable(davTazamonResponse.getMultiStatus())
                .map(MultiStatus::getResponse)
                .orElse(Collections.emptyList())
                .stream().filter(this::filterResponse)
                .map(Response::getPropstat)
                .map(PropertyStatus::getProperty)
                .map(Property::getPropertyType)
                .map(this::parsePropertyType)
                .collect(Collectors.toList());
    }

    private boolean filterResponse(Response response) {
        return Optional.ofNullable(response)
                .map(Response::getPropstat)
                .map(PropertyStatus::getStatus)
                .filter(
                        status -> OK.getStatus().equals(status)
                ).isPresent();
    }

    private Calendar parsePropertyType(PropertyType propertyType) {
        if (!(propertyType instanceof DisplayName)) {
            throw new UserParseException("DAV PropertyType must be instance of DisplayName");
        }
        DisplayName displayName = ((DisplayName) propertyType);
        return Calendar.builder()
                .name(extractDisplayName(displayName))
                .build();
    }

    private String extractDisplayName(DisplayName displayName) {
        return Optional.ofNullable(displayName.getValue())
                .orElseThrow(
                        () -> new UserParseException("DisplayName value can't be null or missing")
                );
    }
}
