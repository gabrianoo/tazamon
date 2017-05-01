package com.tazamon.client.http;

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
@XmlRootElement(name = "propstat")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyStatus {

    @XmlElement(name = "prop", namespace = "DAV:")
    private Property property;
    @XmlElement(name = "status", namespace = "DAV:")
    private String status;
}
