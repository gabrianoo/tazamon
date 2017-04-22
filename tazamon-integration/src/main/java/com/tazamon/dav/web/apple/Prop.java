package com.tazamon.dav.web.apple;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Prop {

    @XmlElement(name = "current-user-principal", namespace = "DAV:", nillable = true)
    private CurrentUserPrincipal currentUserPrincipal;
}
