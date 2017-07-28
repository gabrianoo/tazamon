package com.gability.tazamon.client.dav.xml;


import lombok.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompFilter {

    @XmlAttribute
    private String name;
    @XmlElement(name = "comp-filter", namespace = "urn:ietf:params:xml:ns:caldav")
    private CompFilter compFilter;
}
