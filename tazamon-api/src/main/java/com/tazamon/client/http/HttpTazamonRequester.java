package com.tazamon.client.http;

import java.util.Optional;

/**
 * The goal of this {@link HttpTazamonRequester} is to wrap the boilerplate code performing Web Dav basic operations.
 */
public interface HttpTazamonRequester {

    /**
     * This method is to wrap the Web Dav request and simplify building your request and parsing your response.
     *
     * @param httpTazamonRequest {@link HttpTazamonRequest} entity representing the requirement for performing Web Dav Request.
     * @return {@link HttpTazamonResponse} instance used to get the desired data from the response.
     */
    Optional<HttpTazamonResponse> submitRequest(HttpTazamonRequest httpTazamonRequest);
}
