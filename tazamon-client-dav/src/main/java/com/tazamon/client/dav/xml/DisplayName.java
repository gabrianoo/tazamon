package com.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "displayname", namespace = "DAV:")
public class DisplayName implements PropertyType {

    @XmlValue
    private String value;
}
