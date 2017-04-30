package com.tazamon.calendar;

import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class Calendar {

    @XmlTransient
    private String href;
    @XmlElement(name = "displayname", namespace = "DAV:")
    private String displayName;
}
