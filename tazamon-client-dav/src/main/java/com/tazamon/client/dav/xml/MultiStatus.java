package com.tazamon.client.dav.xml;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "multistatus")
public class MultiStatus {

    @XmlElement(name = "response", namespace = "DAV:")
    private List<Response> response;
}
