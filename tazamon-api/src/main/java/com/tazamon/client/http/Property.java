package com.tazamon.client.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@Getter
@ToString
@EqualsAndHashCode
public class Property {

    @XmlElements({
            @XmlElement(name = "current-user-principal", namespace = "DAV:", type = CurrentUserPrincipal.class),
            @XmlElement(name = "displayname", namespace = "DAV:", type = DisplayName.class)
    })
    private PropertyType propertyType;
}
