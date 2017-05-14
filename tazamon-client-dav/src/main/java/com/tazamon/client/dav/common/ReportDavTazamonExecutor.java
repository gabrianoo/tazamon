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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.HttpReport;
import org.apache.jackrabbit.webdav.security.report.PrincipalMatchReport;
import org.apache.jackrabbit.webdav.version.report.ReportInfo;
import org.apache.jackrabbit.webdav.version.report.ReportType;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.w3c.dom.Element;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.util.Optional;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.jackrabbit.webdav.DavConstants.DEPTH_1;

@Slf4j
@Named
public class ReportDavTazamonExecutor implements DavTazamonExecutor {

    private static final Namespace CAL_DAV_NAMESPACE =
            Namespace.getNamespace("C", "urn:ietf:params:xml:ns:caldav");
    private static final ReportType CALENDAR_QUERY = ReportType.register(
            "calendar-query", CAL_DAV_NAMESPACE, PrincipalMatchReport.class
    );
    private final HttpClient httpClient;
    private final XmlProcessor xmlProcessor;
    private final DocumentBuilder documentBuilder;

    @Inject
    public ReportDavTazamonExecutor(
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
                .map(this::executeReportRequest)
                .flatMap(
                        element -> xmlProcessor.fromXml(element, MultiStatus.class)
                ).map(
                        multiStatus -> new DefaultDavTazamonResponse(multiStatus, davTazamonRequest)
                );
    }

    private Element executeReportRequest(DavTazamonRequest davTazamonRequest) {
        Element element = null;
        try {
            HttpReport httpReport = new HttpReport(
                    davTazamonRequest.getServerUrl(),
                    new ReportInfo(CALENDAR_QUERY, DEPTH_1)
            );
            httpReport.setEntity(new StringEntity(davTazamonRequest.getRequestBody(), ContentType.APPLICATION_XML));
            httpReport.addHeader(AUTHORIZATION, davTazamonRequest.getBase64EncodeAuthToken());
            HttpResponse httpResponse = httpClient.execute(httpReport);
            element = httpReport.getResponseBodyAsMultiStatus(httpResponse).toXml(documentBuilder.newDocument());
        } catch (DavException | IOException e) {
            log.error(e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
        }
        return element;
    }
}
