package com.otasys.tazamon.web.dav;

import com.otasys.tazamon.template.FreeMarkerTemplateService;
import com.otasys.tazamon.xml.XmlConversionService;
import freemarker.template.Configuration;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/14-Introducing-CalDAV-Part-I.html">Introducing CalDAV Part-I</a>
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/15-Introducing-CalDAV-Part-II.html">Introducing CalDAV Part-II</a>
 */
public class DefaultWebDavService implements WebDavService {

    private Configuration freemarkerConfiguration;
    private XmlConversionService xmlConversionService;
    private FreeMarkerTemplateService freeMarkerTemplateService;
    private WebDavHttpMethods webDavHttpMethods;
    private static final String SERVER_URI = "https://p01-contacts.icloud.com/";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    public DefaultWebDavService(
            Configuration freemarkerConfiguration,
            XmlConversionService xmlConversionService,
            FreeMarkerTemplateService freeMarkerTemplateService,
            WebDavHttpMethods webDavHttpMethods
    ) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.xmlConversionService = xmlConversionService;
        this.freeMarkerTemplateService = freeMarkerTemplateService;
        this.webDavHttpMethods = webDavHttpMethods;
    }

    @Override
    public Boolean isWebDavSupported() {
        OptionsMethod options = new OptionsMethod(SERVER_URI);
        DavMethodBase result = webDavHttpMethods.requestWithHeaders(options);
        if (result == null) {
            LOGGER.error("Error occurred while trying to check the server support for WEB DAV (false will be returned).");
            return false;
        } else {
            Header allowHeader = result.getResponseHeader("Allow");
            String allowHeaderValue = allowHeader.getValue();
            return allowHeaderValue.contains("REPORT")
                    && allowHeaderValue.contains("PROPFIND")
                    && allowHeaderValue.contains("PROPPATCH");
        }
    }

    @Override
    public String getUserPrincipalUnifiedId(String basicAuthToken) {
//        PropFindMethod propFind = null;
//        try {
//            propFind = new PropFindMethod(SERVER_URI,
//                    DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_0);
//            propFind.addRequestHeader("Authorization", basicAuthToken);
//            propFind.setRequestBody(
//                    xmlConversionService.loadXmlDocumentFromString(
//                            freeMarkerTemplateService.processTemplateIntoString(
//                                    freemarkerConfiguration.getTemplate("iCloudPrincipal.ftl"),
//                                    null
//                            )
//                    )
//            );
//            httpClient.executeMethod(hostConfiguration, propFind);
//            MultiStatus multiStatus = propFind.getResponseBodyAsMultiStatus();
//            LOGGER.info("{}", multiStatus.getResponses()[0].getProperties(200).getContent());
//        } catch (IOException | DavException e) {
//            LOGGER.error("Error occurred while trying to fetch server WEB DAV properties.");
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Exception Details: ", e);
//            }
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        } finally {
//            if (propFind != null) {
//                propFind.releaseConnection();
//            }
//        }
        return "";
    }
}
