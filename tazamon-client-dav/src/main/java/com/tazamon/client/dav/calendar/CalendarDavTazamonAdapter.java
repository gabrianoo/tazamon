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
import java.util.stream.Stream;

import static com.tazamon.client.dav.common.Status.OK;

@Named
public class CalendarDavTazamonAdapter implements DavTazamonAdapter<List<Calendar>> {

    @Override
    public List<Calendar> adapt(@NonNull DavTazamonResponse davTazamonResponse) {
        return Optional.ofNullable(davTazamonResponse.getMultiStatus())
                .map(MultiStatus::getResponse)
                .orElse(Collections.emptyList())
                .stream().filter(this::filterResponse)
                .flatMap(this::parseResponse)
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

    private Stream<Calendar> parseResponse(Response response) {
        String selfLink = response.getHref();
        return Optional.ofNullable(response.getPropstat().getProperty())
                .map(Property::getPropertyType)
                .map(propertyType -> parsePropertyType(propertyType, selfLink))
                .orElse(Stream.empty());
    }

    private Stream<Calendar> parsePropertyType(PropertyType propertyType, String selfLink) {
        if (!(propertyType instanceof DisplayName)) {
            throw new UserParseException("DAV PropertyType must be instance of DisplayName");
        }
        DisplayName displayName = ((DisplayName) propertyType);
        return Stream.of(
                Calendar.builder()
                        .selfLink(selfLink)
                        .name(extractDisplayName(displayName))
                        .build()
        );
    }

    private String extractDisplayName(DisplayName displayName) {
        return Optional.ofNullable(displayName.getValue())
                .orElseThrow(
                        () -> new UserParseException("DisplayName value can't be null or missing")
                );
    }
}
