package com.tazamon.dav.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Optional;

import static org.apache.commons.httpclient.HttpStatus.SC_OK;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.jackrabbit.webdav.DavConstants.DEPTH_0;
import static org.apache.jackrabbit.webdav.DavConstants.PROPFIND_ALL_PROP;

/**
 * Implementation of {@link WebDavRequest} using the {@link HttpPropfind}.
 */
@Slf4j
@Named
public class PropFindWebDavRequest implements WebDavRequest {

    private final HttpClient httpClient;

    @Inject
    public PropFindWebDavRequest(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<DavResponse> submitRequest(RequestWrapper requestWrapper) {
        Optional<DavResponse> responseWrapperOptional = Optional.empty();
        HttpPropfind httpPropfind;
        try {
            httpPropfind = new HttpPropfind(requestWrapper.getServerUrl(), PROPFIND_ALL_PROP, DEPTH_0);
            httpPropfind.addHeader(AUTHORIZATION, requestWrapper.getBase64EncodeAuthToken());
            httpPropfind.setEntity(requestWrapper.getHttpEntity());
            HttpResponse httpResponse = httpClient.execute(httpPropfind);
            MultiStatusResponse[] multiStatusResponses
                    = httpPropfind.getResponseBodyAsMultiStatus(httpResponse).getResponses();
            if (multiStatusResponses.length >= 1) {
                DavPropertySet davPropertySet = multiStatusResponses[0].getProperties(SC_OK);
                responseWrapperOptional = Optional.of(DavResponse.builder().davPropertySet(davPropertySet).build());
            }
        } catch (DavException | IOException e) {
            log.error("", e);
        }
        return responseWrapperOptional;
    }
}
