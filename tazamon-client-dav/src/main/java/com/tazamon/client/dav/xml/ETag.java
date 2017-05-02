package com.tazamon.client.dav.xml;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "getetag", namespace = "DAV:")
public class ETag implements PropertyType {

    @XmlValue
    private String value;
}
