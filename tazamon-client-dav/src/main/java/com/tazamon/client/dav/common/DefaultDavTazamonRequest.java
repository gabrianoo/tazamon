package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonRequest;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * The goal of this {@link DefaultDavTazamonRequest} is to wrap the Web Dav request for safe refactoring/maintaining later.
 */
@Slf4j
@Value
public class DefaultDavTazamonRequest implements DavTazamonRequest {

    final String COLON = ":";
    final String SPACE = " ";
    final String BASIC = "Basic";
    String base64EncodeAuthToken;
    String requestBody;
    String serverUrl;

    public DefaultDavTazamonRequest(
            @NonNull String base64EncodeAuthToken,
            @NonNull String requestBody,
            @NonNull String serverUrl
    ) {
        this.base64EncodeAuthToken = base64EncodeAuthToken;
        this.requestBody = requestBody;
        this.serverUrl = serverUrl;
    }

    public DefaultDavTazamonRequest(
            @NonNull String email,
            @NonNull String password,
            @NonNull String requestBody,
            @NonNull String serverUrl) {
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
    @NonNull
    private String base64EncodeAuthToken(String email, String password) {
        String authString = String.join(COLON, email, password);
        byte[] encodedAuthBytes = Base64.getEncoder().encode(authString.getBytes());
        String encodedAuthString = new String(encodedAuthBytes);
        String base64EncodeAuthToken = String.join(SPACE, BASIC, encodedAuthString);
        if (log.isDebugEnabled()) {
            log.debug("Base64 encoded auth string: [{}]", base64EncodeAuthToken);
        }
        return base64EncodeAuthToken;
    }
}
