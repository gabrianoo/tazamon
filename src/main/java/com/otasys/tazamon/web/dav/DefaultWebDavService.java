package com.otasys.tazamon.web.dav;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

/**
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/14-Introducing-CalDAV-Part-I.html">Introducing CalDAV Part-I</a>
 * @see <a href="http://blogs.nologin.es/rickyepoderi/index.php?/archives/15-Introducing-CalDAV-Part-II.html">Introducing CalDAV Part-II</a>
 */
@Named
public class DefaultWebDavService implements WebDavService {

    private HttpClient httpClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    @Inject
    public DefaultWebDavService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Boolean isWebDavSupported() {
        OptionsMethod options = null;
        try {
            options = new OptionsMethod("https://p01-contacts.icloud.com/");
            httpClient.executeMethod(options);
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
}
