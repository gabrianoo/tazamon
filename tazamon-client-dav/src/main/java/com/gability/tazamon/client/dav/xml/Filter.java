package com.gability.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    @XmlElement(name = "comp-filter", namespace = "urn:ietf:params:xml:ns:caldav")
    private CompFilter compFilter;
}
