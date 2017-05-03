package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonRequest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDavTazamonRequestTest {

    private static final String DEFAULT_USERNAME = "ME";
    private static final String DEFAULT_PASSWORD = "SECRET";
    private static final String DEFAULT_REQUEST_BODY = "BODY";
    private static final String DEFAULT_SERVER_URL = "URL";
    private static final String DEFAULT_BASE_64_TOKEN = "Basic TUU6U0VDUkVU";

    @Test
    public void givenUsernamePasswordWhenRequestIsCreatedThenBase64TokenIsGenerated() {
        DavTazamonRequest davTazamonRequest = new DefaultDavTazamonRequest(
                DEFAULT_USERNAME,
                DEFAULT_PASSWORD,
                DEFAULT_REQUEST_BODY,
                DEFAULT_SERVER_URL
        );
        assertThat(davTazamonRequest.getBase64EncodeAuthToken()).isEqualTo(DEFAULT_BASE_64_TOKEN);
    }

    @Test
    public void givenUsernamePasswordRequestWhenCompareToBase64TokenRequestThenItShouldBeTheSame() {
        DavTazamonRequest usernamePasswordRequest = new DefaultDavTazamonRequest(
                DEFAULT_USERNAME,
                DEFAULT_PASSWORD,
                DEFAULT_REQUEST_BODY,
                DEFAULT_SERVER_URL
        );
        DavTazamonRequest base64TokenRequest = new DefaultDavTazamonRequest(
                DEFAULT_BASE_64_TOKEN,
                DEFAULT_REQUEST_BODY,
                DEFAULT_SERVER_URL
        );
        assertThat(usernamePasswordRequest).isEqualTo(base64TokenRequest);
    }
}
