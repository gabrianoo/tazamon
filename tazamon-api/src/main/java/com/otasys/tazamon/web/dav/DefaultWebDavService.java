package com.otasys.tazamon.web.dav;

import com.otasys.tazamon.xml.XmlConversionService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static com.otasys.tazamon.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

/**
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/14-Introducing-CalDAV-Part-I.html">Introducing CalDAV Part-I</a>
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/15-Introducing-CalDAV-Part-II.html">Introducing CalDAV Part-II</a>
 */
public class DefaultWebDavService implements WebDavService {

    private HttpClient httpClient;
    private Configuration freemarkerConfiguration;
    private XmlConversionService xmlConversionService;
    private HostConfiguration hostConfiguration;
    private static final String SERVER_URI = "https://p01-contacts.icloud.com/";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    public DefaultWebDavService(
            HttpClient httpClient,
            Configuration freemarkerConfiguration,
            XmlConversionService xmlConversionService
    ) {
        this.httpClient = httpClient;
        this.freemarkerConfiguration = freemarkerConfiguration;
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
                            processTemplateIntoString(freemarkerConfiguration.getTemplate("iCloudPrincipal.ftl"), null)
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
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if (propFind != null) {
                propFind.releaseConnection();
            }
        }
        return "";
    }
}
