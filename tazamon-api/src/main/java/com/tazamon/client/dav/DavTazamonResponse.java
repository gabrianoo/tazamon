package com.tazamon.client.dav;

public interface DavTazamonResponse {

    MultiStatus getMultiStatus();

    DavTazamonRequest getDavTazamonRequest();
}
