package com.tazamon.client.http;

import com.tazamon.common.XmlProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.jackrabbit.webdav.DavConstants.DEPTH_1;
import static org.apache.jackrabbit.webdav.DavConstants.PROPFIND_ALL_PROP;

/**
 * Implementation of {@link HttpTazamonRequester} using the {@link HttpPropfind}.
 */
@Slf4j
@Named
public class PropFindHttpTazamonRequester implements HttpTazamonRequester {

    private final HttpClient httpClient;
    private final XmlProcessor xmlProcessor;

    @Inject
    public PropFindHttpTazamonRequester(
            HttpClient httpClient,
            XmlProcessor xmlProcessor
    ) {
        this.httpClient = httpClient;
        this.xmlProcessor = xmlProcessor;
    }

    @Override
    public Optional<HttpTazamonResponse> submitRequest(HttpTazamonRequest httpTazamonRequest) {
        Optional<HttpTazamonResponse> responseWrapperOptional = Optional.empty();
        HttpPropfind httpPropfind;
        try {
            httpPropfind = new HttpPropfind(httpTazamonRequest.getServerUrl(), PROPFIND_ALL_PROP, DEPTH_1);
            httpPropfind.addHeader(AUTHORIZATION, httpTazamonRequest.getBase64EncodeAuthToken());
            httpPropfind.setEntity(new StringEntity(httpTazamonRequest.getRequestBody()));
            HttpResponse httpResponse = httpClient.execute(httpPropfind);
            MultiStatus multiStatus = xmlProcessor.fromXml(
                    httpPropfind.getResponseBodyAsMultiStatus(httpResponse).toXml(XmlProcessor.buildXmlNode()),
                    MultiStatus.class
            );
            responseWrapperOptional = Optional.of(new DefaultHttpTazamonResponse(multiStatus, httpTazamonRequest));
        } catch (JAXBException | ParserConfigurationException | DavException | IOException e) {
            log.error("", e);
        }
        return responseWrapperOptional;
    }
}
