package com.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "calendar-data", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalendarData implements PropertyType {

    @XmlValue
    private String value;
}
