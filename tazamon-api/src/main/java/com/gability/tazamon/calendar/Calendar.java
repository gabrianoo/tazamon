package com.gability.tazamon.calendar;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Calendar {

    private String selfLink;
    private String name;
}
