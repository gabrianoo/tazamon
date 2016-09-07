package com.otasys.tazamon.web.dav;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultWebDavHttpMethods implements WebDavHttpMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavHttpMethods.class);
    private HttpClient httpClient;
    private HostConfiguration hostConfiguration;

    public DefaultWebDavHttpMethods(HttpClient httpClient, HostConfiguration hostConfiguration) {
        this.httpClient = httpClient;
        this.hostConfiguration = hostConfiguration;
    }

    @Override
    public DavMethodBase basicRequest(DavMethodBase davMethodBase) {
        try {
            httpClient.executeMethod(hostConfiguration, davMethodBase);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Request status {}", davMethodBase.getStatusLine());
            }
            return davMethodBase;
        } catch (IOException e) {
            LOGGER.error("Error occurred while requesting WEB DAV resources (null will be returned).");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception Details: ", e);
            }
            return null;
        } finally {
            if (davMethodBase != null) {
                davMethodBase.releaseConnection();
            }
        }
    }
}
