package com.otasys.tazamon.web.dav;

public interface WebDavService {

    Boolean isWebDavSupported();

    String getUserPrincipalUnifiedId(String basicAuthToken);
}
