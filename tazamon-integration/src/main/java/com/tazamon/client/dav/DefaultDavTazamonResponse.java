package com.tazamon.client.dav;

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
