package com.otasys.tazamon.web.dav;

import com.otasys.tazamon.template.FreeMarkerTemplateService;
import com.otasys.tazamon.xml.XmlConversionService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.httpclient.Header;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/14-Introducing-CalDAV-Part-I.html">Introducing CalDAV Part-I</a>
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/15-Introducing-CalDAV-Part-II.html">Introducing CalDAV Part-II</a>
 */
public class DefaultWebDavService implements WebDavService {

    private Configuration freemarkerConfiguration;
    private XmlConversionService xmlConversionService;
    private FreeMarkerTemplateService freeMarkerTemplateService;
    private WebDavHttpMethods webDavHttpMethods;
    private Unmarshaller unmarshaller;
    private static final String SERVER_URI = "https://p01-contacts.icloud.com/";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    public DefaultWebDavService(
            Configuration freemarkerConfiguration,
            XmlConversionService xmlConversionService,
            FreeMarkerTemplateService freeMarkerTemplateService,
            WebDavHttpMethods webDavHttpMethods,
            Unmarshaller unmarshaller
    ) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.xmlConversionService = xmlConversionService;
        this.freeMarkerTemplateService = freeMarkerTemplateService;
        this.webDavHttpMethods = webDavHttpMethods;
        this.unmarshaller = unmarshaller;
    }

    @Override
    public Boolean isWebDavSupported() {
        OptionsMethod options = new OptionsMethod(SERVER_URI);
        DavMethodBase result = webDavHttpMethods.basicRequest(options);
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
        String principal = "";
        try {
            PropFindMethod propFind = new PropFindMethod(SERVER_URI,
                    DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_0);
            propFind.addRequestHeader("Authorization", basicAuthToken);
            propFind.setRequestBody(
                    xmlConversionService.loadXmlDocumentFromString(
                            freeMarkerTemplateService.processTemplateIntoString(
                                    freemarkerConfiguration.getTemplate("iCloudPrincipal.ftl"),
                                    null
                            )
                    )
            );
            DavMethodBase result = webDavHttpMethods.basicRequest(propFind);
            MultiStatus multiStatus = result.getResponseBodyAsMultiStatus();
            if (multiStatus.getResponses().length > 0) {
                DavPropertySet successDavProperties = multiStatus.getResponses()[0].getProperties(200);
                if (!successDavProperties.isEmpty()) {
                    DavProperty currentUserPrincipalProperty = successDavProperties.get("current-user-principal");
                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                    Document doc = docBuilder.newDocument();
                    JAXBElement<UserPrincipal> userPrincipalJAXBElement = unmarshaller.unmarshal(currentUserPrincipalProperty.toXml(doc), UserPrincipal.class);
                    UserPrincipal userPrincipal = userPrincipalJAXBElement.getValue();
                    principal = userPrincipal.getPrincipal();
                }
            }
        } catch (ParserConfigurationException | JAXBException | IOException | DavException | TemplateException e) {
            LOGGER.error("Error occurred while trying to fetch server WEB DAV properties.");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception Details: ", e);
            }
        }
        return principal;
    }
}
