package com.gability.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "calendar-query", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalendarQuery {

    @XmlAnyElement(lax = true)
    @XmlElementWrapper(name = "prop", namespace = "DAV:")
    private List<PropertyType> properties;
    @XmlElement(name = "filter", namespace = "urn:ietf:params:xml:ns:caldav")
    private Filter filter;
}
