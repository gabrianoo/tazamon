package com.tazamon.client.dav.xml;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "displayname", namespace = "DAV:")
public class DisplayName implements PropertyType {

    @XmlValue
    private String value;
}
