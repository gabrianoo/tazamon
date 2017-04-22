package com.tazamon.dav.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import javax.inject.Named;
import java.io.IOException;
import java.util.Optional;

import static org.apache.commons.httpclient.HttpStatus.SC_OK;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.jackrabbit.webdav.DavConstants.DEPTH_0;
import static org.apache.jackrabbit.webdav.DavConstants.PROPFIND_ALL_PROP;

/**
 * Implementation of {@link WebDavRequest} using the {@link PropFindMethod}.
 */
@Slf4j
@Named
public class PropFindWebDavRequest implements WebDavRequest {

    private final HttpClient httpClient = new HttpClient();

    @Override
    public Optional<DavResponse> submitRequest(RequestWrapper requestWrapper) {
        Optional<DavResponse> responseWrapperOptional = Optional.empty();
        PropFindMethod propFind;
        try {
            propFind = new PropFindMethod(requestWrapper.getServerUrl(), PROPFIND_ALL_PROP, DEPTH_0);
            propFind.addRequestHeader(AUTHORIZATION, requestWrapper.getBase64EncodeAuthToken());
            propFind.setRequestEntity(requestWrapper.getRequestEntity());
            httpClient.executeMethod(propFind);
            MultiStatusResponse[] multiStatusResponses = propFind.getResponseBodyAsMultiStatus().getResponses();
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
