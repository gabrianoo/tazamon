package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonRequest;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

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

    public DefaultDavTazamonRequest(String base64EncodeAuthToken, String requestBody, String serverUrl) {
        this.base64EncodeAuthToken = base64EncodeAuthToken;
        this.requestBody = requestBody;
        this.serverUrl = serverUrl;
    }

    public DefaultDavTazamonRequest(String email, String password, String requestBody, String serverUrl) {
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
