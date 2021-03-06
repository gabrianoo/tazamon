package com.gability.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "response")
public class Response {

    @XmlElement(name = "href", namespace = "DAV:")
    private String href;
    @XmlElement(name = "propstat", namespace = "DAV:")
    private PropertyStatus propstat;
}
