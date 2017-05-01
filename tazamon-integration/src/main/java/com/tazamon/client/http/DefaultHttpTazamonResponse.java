package com.tazamon.client.http;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The goal of this {@link DefaultHttpTazamonResponse} is to wrap the Web Dav response for safe refactoring/maintaining later.
 */
@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
class DefaultHttpTazamonResponse implements HttpTazamonResponse {

    private MultiStatus multiStatus;
    private HttpTazamonRequest httpTazamonRequest;
}
