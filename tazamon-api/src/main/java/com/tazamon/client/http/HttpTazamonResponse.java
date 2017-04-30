package com.tazamon.client.http;

public interface HttpTazamonResponse<P> {

    P getPropertySet();

    HttpTazamonRequest getHttpTazamonRequest();
}
