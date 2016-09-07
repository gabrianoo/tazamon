package com.otasys.tazamon.web.dav;

import org.apache.jackrabbit.webdav.client.methods.DavMethodBase;

public interface WebDavHttpMethods {

    DavMethodBase basicRequest(DavMethodBase davMethodBase);
}
