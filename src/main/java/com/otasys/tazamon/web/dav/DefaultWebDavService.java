package com.otasys.tazamon.web.dav;

import com.otasys.tazamon.xml.XmlConversionService;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URI;

/**
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/14-Introducing-CalDAV-Part-I.html">Introducing CalDAV Part-I</a>
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/15-Introducing-CalDAV-Part-II.html">Introducing CalDAV Part-II</a>
 */
@Named
public class DefaultWebDavService implements WebDavService {

    private HttpClient httpClient;
    private VelocityEngine velocityEngine;
    private XmlConversionService xmlConversionService;
    private HostConfiguration hostConfiguration;
    private static final String SERVER_URI = "https://p01-contacts.icloud.com/";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    @Inject
    public DefaultWebDavService(
            HttpClient httpClient,
            VelocityEngine velocityEngine,
            XmlConversionService xmlConversionService
    ) {
        this.httpClient = httpClient;
        this.velocityEngine = velocityEngine;
        this.xmlConversionService = xmlConversionService;
        this.hostConfiguration = new HostConfiguration();
        String httpsProxyServerUriString = System.getenv("HTTPS_PROXY");
        if (httpsProxyServerUriString != null && !httpsProxyServerUriString.isEmpty()) {
            URI httpsProxyServerURI = URI.create(System.getenv("HTTPS_PROXY"));
            this.hostConfiguration.setProxy(httpsProxyServerURI.getHost(), httpsProxyServerURI.getPort());
        }
    }

    @Override
    public Boolean isWebDavSupported() {
        OptionsMethod options = null;
        try {
            options = new OptionsMethod(SERVER_URI);
            httpClient.executeMethod(hostConfiguration, options);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Option request status {}", options.getStatusLine());
            }
            Header allowHeader = options.getResponseHeader("Allow");
            String allowHeaderValue = allowHeader.getValue();
            return allowHeaderValue.contains("REPORT")
                    && allowHeaderValue.contains("PROPFIND")
                    && allowHeaderValue.contains("PROPPATCH");
        } catch (IOException e) {
            LOGGER.error("Error occurred while trying to check the server support for WEB DAV (false will be returned).");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception Details: ", e);
            }
            return false;
        } finally {
            if (options != null) {
                options.releaseConnection();
            }
        }
    }

    @Override
    public String getUserPrincipalUnifiedId(String basicAuthToken) {
        PropFindMethod propFind = null;
        try {
            propFind = new PropFindMethod(SERVER_URI,
                    DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_0);
            propFind.addRequestHeader("Authorization", basicAuthToken);
            propFind.setRequestBody(
                    xmlConversionService.loadXmlDocumentFromString(
                            VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "iCloudPrincipal.vm", "UTF-8", null)
                    )
            );
            httpClient.executeMethod(hostConfiguration, propFind);
            MultiStatus multiStatus = propFind.getResponseBodyAsMultiStatus();
            LOGGER.info("{}", multiStatus.getResponses()[0].getProperties(200).getContent());
        } catch (IOException | DavException e) {
            LOGGER.error("Error occurred while trying to fetch server WEB DAV properties.");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception Details: ", e);
            }
        } finally {
            if (propFind != null) {
                propFind.releaseConnection();
            }
        }
        return "";
    }
}
