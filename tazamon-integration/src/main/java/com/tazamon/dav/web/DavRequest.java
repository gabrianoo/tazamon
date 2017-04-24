package com.tazamon.dav.web;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.jackrabbit.webdav.client.methods.XmlRequestEntity;

import java.io.IOException;

/**
 * The goal of this {@link DavRequest} is to wrap the Web Dav request for safe refactoring/maintaining later.
 */
@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DavRequest {

    private static final String COLON = ":";
    private static final String SPACE = " ";
    private static final String BASIC = "Basic";
    private String base64EncodeAuthToken;
    private HttpEntity httpEntity;
    private String serverUrl;

    /**
     * A builder class for {@link DavRequest}.
     */
    public static class RequestWrapperBuilder {

        private String base64EncodeAuthToken;
        private HttpEntity httpEntity;
        private String serverUrl;

        private RequestWrapperBuilder() {
        }

        /**
         * This method is used to calculate the base64 encoding for the basic authentication token.
         *
         * @param email    used as a username to authenticate the user.
         * @param password used to connect to the required service, it can be application password
         *                 or user password if the user didn't enable two factor authentication.
         */
        public RequestWrapperBuilder base64EncodeAuthToken(String email, String password) {
            String authString = String.join(COLON, email, password);
            base64EncodeAuthToken = String.join(SPACE, BASIC, Base64.encodeBase64String(authString.getBytes()));
            if (log.isDebugEnabled()) {
                log.debug("Base64 encoded auth string: [{}]", base64EncodeAuthToken);
            }
            return this;
        }

        /**
         * This method is used if you already have the base64 encoded basic authentication token.
         *
         * @param base64EncodeAuthToken the base64EncodeAuthToken used in authentication.
         */
        public RequestWrapperBuilder base64EncodeAuthToken(String base64EncodeAuthToken) {
            this.base64EncodeAuthToken = base64EncodeAuthToken;
            return this;
        }

        /**
         * This method is used to generate the {@link RequestEntity} using {@link XmlRequestEntity}.
         *
         * @param requestBody used as the request body.
         */
        public RequestWrapperBuilder httpEntity(String requestBody) {
            try {
                this.httpEntity = new StringEntity(requestBody);
            } catch (IOException e) {
                log.error("", e);
            }
            return this;
        }

        /**
         * This is the server this request will hit.
         *
         * @param serverUrl to be used in this request.
         */
        public RequestWrapperBuilder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        /**
         * Build the {@link DavRequest} using the provided data in the builder.
         *
         * @return {@link DavRequest} ready to be used by {@link WebDavRequest} implementations.
         */
        public DavRequest build() {
            return new DavRequest(base64EncodeAuthToken, httpEntity, serverUrl);
        }
    }

    /**
     * Provide a new {@link RequestWrapperBuilder} to assist in building a {@link DavRequest} instance ready to be
     * used by {@link WebDavRequest} implementations.
     *
     * @return new empty instance of {@link RequestWrapperBuilder}.
     */
    public static RequestWrapperBuilder builder() {
        return new RequestWrapperBuilder();
    }
}
