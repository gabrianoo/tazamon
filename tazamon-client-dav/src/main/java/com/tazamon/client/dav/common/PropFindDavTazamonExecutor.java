package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.MultiStatus;
import com.tazamon.xml.XmlProcessor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.w3c.dom.Element;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.util.Optional;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.jackrabbit.webdav.DavConstants.DEPTH_1;
import static org.apache.jackrabbit.webdav.DavConstants.PROPFIND_ALL_PROP;

/**
 * Implementation of {@link DavTazamonExecutor} using the {@link HttpPropfind}.
 */
@Slf4j
@Named
public final class PropFindDavTazamonExecutor implements DavTazamonExecutor {

    private final HttpClient httpClient;
    private final XmlProcessor xmlProcessor;
    private final DocumentBuilder documentBuilder;

    @Inject
    public PropFindDavTazamonExecutor(
            HttpClient httpClient,
            XmlProcessor xmlProcessor,
            DocumentBuilder documentBuilder
    ) {
        this.httpClient = httpClient;
        this.xmlProcessor = xmlProcessor;
        this.documentBuilder = documentBuilder;
    }

    @Override
    public Optional<DavTazamonResponse> execute(@NonNull DavTazamonRequest davTazamonRequest) {
        return Optional.of(davTazamonRequest)
                .map(this::executePropFindRequest)
                .flatMap(
                        element -> xmlProcessor.fromXml(element, MultiStatus.class)
                ).map(
                        multiStatus -> new DefaultDavTazamonResponse(multiStatus, davTazamonRequest)
                );
    }

    private Element executePropFindRequest(DavTazamonRequest davTazamonRequest) {
        Element element = null;
        try {
            HttpPropfind httpPropfind = new HttpPropfind(davTazamonRequest.getServerUrl(), PROPFIND_ALL_PROP, DEPTH_1);
            httpPropfind.addHeader(AUTHORIZATION, davTazamonRequest.getBase64EncodeAuthToken());
            httpPropfind.setEntity(new StringEntity(davTazamonRequest.getRequestBody()));
            HttpResponse httpResponse = httpClient.execute(httpPropfind);
            element = httpPropfind.getResponseBodyAsMultiStatus(httpResponse).toXml(documentBuilder.newDocument());
        } catch (DavException | IOException e) {
            log.error(e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
        }
        return element;
    }
}
