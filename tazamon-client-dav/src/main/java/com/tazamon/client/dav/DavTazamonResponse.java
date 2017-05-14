package com.tazamon.client.dav;

import com.tazamon.client.dav.xml.MultiStatus;

public interface DavTazamonResponse {

    MultiStatus getMultiStatus();

    DavTazamonRequest getDavTazamonRequest();
}
