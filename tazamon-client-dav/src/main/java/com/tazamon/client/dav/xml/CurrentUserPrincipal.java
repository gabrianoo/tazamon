package com.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "current-user-principal")
public class CurrentUserPrincipal implements PropertyType {

    @XmlElement(name = "href", namespace = "DAV:")
    private String href;
}
