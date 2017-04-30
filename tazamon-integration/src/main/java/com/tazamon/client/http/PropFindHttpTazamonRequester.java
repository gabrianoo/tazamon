package com.tazamon.client.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
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
 * Implementation of {@link HttpTazamonRequester} using the {@link HttpPropfind}.
 */
@Slf4j
@Named
public class PropFindHttpTazamonRequester implements HttpTazamonRequester {

    private final HttpClient httpClient;

    @Inject
    public PropFindHttpTazamonRequester(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<HttpTazamonResponse> submitRequest(HttpTazamonRequest httpTazamonRequest) {
        Optional<HttpTazamonResponse> responseWrapperOptional = Optional.empty();
        HttpPropfind httpPropfind;
        try {
            httpPropfind = new HttpPropfind(httpTazamonRequest.getServerUrl(), PROPFIND_ALL_PROP, DEPTH_0);
            httpPropfind.addHeader(AUTHORIZATION, httpTazamonRequest.getBase64EncodeAuthToken());
            httpPropfind.setEntity(new StringEntity(httpTazamonRequest.getRequestBody()));
            HttpResponse httpResponse = httpClient.execute(httpPropfind);
            MultiStatusResponse[] multiStatusResponses
                    = httpPropfind.getResponseBodyAsMultiStatus(httpResponse).getResponses();
            if (multiStatusResponses.length >= 1) {
                DavPropertySet davPropertySet = multiStatusResponses[0].getProperties(SC_OK);
                responseWrapperOptional = Optional.of(new DefaultHttpTazamonResponse(davPropertySet, httpTazamonRequest));
            }
        } catch (DavException | IOException e) {
            log.error("", e);
        }
        return responseWrapperOptional;
    }
}
