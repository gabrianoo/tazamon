package com.gability.tazamon.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private String selfLink;
    private String principal;
    private String base64EncodeAuthToken;
}
