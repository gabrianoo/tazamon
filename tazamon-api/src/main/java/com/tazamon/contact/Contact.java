package com.tazamon.contact;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
class Contact {

    private String name;
    private String mobileNumber;
}
