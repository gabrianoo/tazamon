package com.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "propstat")
public class PropertyStatus {

    @XmlElement(name = "prop", namespace = "DAV:")
    private Property property;
    @XmlElement(name = "status", namespace = "DAV:")
    private String status;
}
