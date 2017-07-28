package com.gability.tazamon.client.dav;

import com.gability.tazamon.client.dav.xml.MultiStatus;

public interface DavTazamonResponse {

    MultiStatus getMultiStatus();

    DavTazamonRequest getDavTazamonRequest();
}
