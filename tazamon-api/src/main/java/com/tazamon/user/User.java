package com.tazamon.user;

import com.tazamon.calendar.Calendar;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private String principal;
    private String base64EncodeAuthToken;
    private List<Calendar> calendars;
}
