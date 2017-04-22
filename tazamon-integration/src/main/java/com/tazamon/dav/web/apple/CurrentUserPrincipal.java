package com.tazamon.dav.web.apple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "current-user-principal")
public class CurrentUserPrincipal {

    @XmlElement(namespace = "DAV:")
    private String href;
}
