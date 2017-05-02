package com.tazamon.client.dav.xml;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "response")
public class Response {

    @XmlElement(name = "href", namespace = "DAV:")
    private String href;
    @XmlElement(name = "propstat", namespace = "DAV:")
    private PropertyStatus propstat;
}
