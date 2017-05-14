package com.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "propfind", namespace = "DAV:")
public class PropertyFind {

    @XmlElement(name = "prop", namespace = "DAV:")
    private Property property;
}
