package com.otasys.tazamon.web.dav;

import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;

public interface WebDavHttpMethods {

    DavMethodBase requestWithHeaders(DavMethodBase davMethodBase);
}
