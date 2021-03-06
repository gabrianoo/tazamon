package com.gability.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @XmlElements({
            @XmlElement(name = "current-user-principal", namespace = "DAV:", type = CurrentUserPrincipal.class, nillable = true),
            @XmlElement(name = "displayname", namespace = "DAV:", type = DisplayName.class, nillable = true),
            @XmlElement(name = "getetag", namespace = "DAV:", type = ETag.class, nillable = true),
            @XmlElement(name = "calendar-data", namespace = "urn:ietf:params:xml:ns:caldav", type = CalendarData.class, nillable = true)
    })
    private PropertyType propertyType;
}
