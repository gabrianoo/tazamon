package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.MultiStatus;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * The goal of this {@link DefaultDavTazamonResponse} is to wrap the Web Dav response for safe refactoring/maintaining later.
 */
@Slf4j
@Value
class DefaultDavTazamonResponse implements DavTazamonResponse {

    MultiStatus multiStatus;
    DavTazamonRequest davTazamonRequest;
}
