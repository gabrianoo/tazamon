package com.tazamon.client.dav;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "propstat")
public class PropertyStatus {

    @XmlElement(name = "prop", namespace = "DAV:")
    private Property property;
    @XmlElement(name = "status", namespace = "DAV:")
    private String status;
}
