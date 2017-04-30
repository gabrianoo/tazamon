package com.tazamon.client.http;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * The goal of this {@link DefaultHttpTazamonRequest} is to wrap the Web Dav request for safe refactoring/maintaining later.
 */
@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DefaultHttpTazamonRequest implements HttpTazamonRequest {

    private static final String COLON = ":";
    private static final String SPACE = " ";
    private static final String BASIC = "Basic";
    private String base64EncodeAuthToken;
    private String requestBody;
    private String serverUrl;

    public DefaultHttpTazamonRequest(String email, String password, String requestBody, String serverUrl) {
        this.base64EncodeAuthToken = base64EncodeAuthToken(email, password);
        this.requestBody = requestBody;
        this.serverUrl = serverUrl;
    }

    /**
     * This method is used to calculate the base64 encoding for the basic authentication token.
     *
     * @param email    used as a username to authenticate the user.
     * @param password used to connect to the required service, it can be application password
     *                 or user password if the user didn't enable two factor authentication.
     */
    private String base64EncodeAuthToken(String email, String password) {
        String authString = String.join(COLON, email, password);
        String base64EncodeAuthToken = String.join(SPACE, BASIC, Base64.encodeBase64String(authString.getBytes()));
        if (log.isDebugEnabled()) {
            log.debug("Base64 encoded auth string: [{}]", base64EncodeAuthToken);
        }
        return base64EncodeAuthToken;
    }
}
