package com.tazamon.client.dav;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "multistatus")
public class MultiStatus {

    @XmlElement(name = "response", namespace = "DAV:")
    private List<Response> response;
}
