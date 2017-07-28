package com.gability.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "getetag", namespace = "DAV:")
public class ETag implements PropertyType {

    @XmlValue
    private String value;
}
