package com.gability.tazamon.client.dav;

import java.util.Optional;

/**
 * The goal of this {@link DavTazamonExecutor} is to wrap the boilerplate code performing Web Dav basic operations.
 */
public interface DavTazamonExecutor {

    /**
     * This method is to wrap the Web Dav request and simplify building your request and parsing your response.
     *
     * @param davTazamonRequest {@link DavTazamonRequest} entity representing the requirement for performing Web Dav Request.
     * @return {@link DavTazamonResponse} instance used to get the desired data from the response.
     */
    Optional<DavTazamonResponse> execute(DavTazamonRequest davTazamonRequest);
}
