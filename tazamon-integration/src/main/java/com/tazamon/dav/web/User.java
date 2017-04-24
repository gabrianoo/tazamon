package com.tazamon.dav.web;

import com.tazamon.dav.cal.Calendar;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "current-user-principal")
public class User {

    @XmlElement(namespace = "DAV:")
    private String href;
    @XmlTransient
    private String principal;
    @XmlTransient
    private String base64EncodeAuthToken;
    @XmlTransient
    private List<Calendar> calendars;
}
