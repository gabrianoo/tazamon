package com.tazamon.client.http;

public interface HttpTazamonRequest {

    String getBase64EncodeAuthToken();

    String getRequestBody();

    String getServerUrl();
}
