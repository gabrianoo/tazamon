package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.MultiStatus;
import lombok.Value;

/**
 * The goal of this {@link DefaultDavTazamonResponse} is to wrap the Web Dav response for safe refactoring/maintaining later.
 */
@Value
public class DefaultDavTazamonResponse implements DavTazamonResponse {

    MultiStatus multiStatus;
    DavTazamonRequest davTazamonRequest;
}
