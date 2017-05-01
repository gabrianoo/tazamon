package com.tazamon.client.dav;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "current-user-principal")
public class CurrentUserPrincipal implements PropertyType {

    @XmlElement(name = "href", namespace = "DAV:")
    private String href;
}