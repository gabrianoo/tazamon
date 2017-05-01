package com.tazamon.client.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "multistatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class MultiStatus {

    @XmlElement(name = "response", namespace = "DAV:")
    private List<Response> response;
}
