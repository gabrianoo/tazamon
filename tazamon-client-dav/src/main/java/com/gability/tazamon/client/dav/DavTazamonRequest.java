package com.gability.tazamon.client.dav;

public interface DavTazamonRequest {

    String getBase64EncodeAuthToken();

    String getRequestBody();

    String getServerUrl();
}
