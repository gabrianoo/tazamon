package com.tazamon.client.http;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * The goal of this {@link DefaultHttpTazamonResponse} is to wrap the Web Dav response for safe refactoring/maintaining later.
 */
@Slf4j
@Value
class DefaultHttpTazamonResponse implements HttpTazamonResponse {

    MultiStatus multiStatus;
    HttpTazamonRequest httpTazamonRequest;
}
