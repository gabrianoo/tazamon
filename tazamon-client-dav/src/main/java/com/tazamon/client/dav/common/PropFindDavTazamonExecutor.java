package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.MultiStatus;
import com.tazamon.xml.XmlProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;

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
    public Optional<DavTazamonResponse> execute(DavTazamonRequest davTazamonRequest) {
        Optional<DavTazamonResponse> responseWrapperOptional = Optional.empty();
        HttpPropfind httpPropfind;
        try {
            httpPropfind = new HttpPropfind(davTazamonRequest.getServerUrl(), PROPFIND_ALL_PROP, DEPTH_1);
            httpPropfind.addHeader(AUTHORIZATION, davTazamonRequest.getBase64EncodeAuthToken());
            httpPropfind.setEntity(new StringEntity(davTazamonRequest.getRequestBody()));
            HttpResponse httpResponse = httpClient.execute(httpPropfind);
            responseWrapperOptional =
                    xmlProcessor.fromXml(
                            httpPropfind
                                    .getResponseBodyAsMultiStatus(httpResponse)
                                    .toXml(
                                            documentBuilder.newDocument()
                                    ),
                            MultiStatus.class
                    ).map(multiStatus -> new DefaultDavTazamonResponse(multiStatus, davTazamonRequest));
        } catch (DavException | IOException e) {
            log.error("", e);
        }
        return responseWrapperOptional;
    }
}
