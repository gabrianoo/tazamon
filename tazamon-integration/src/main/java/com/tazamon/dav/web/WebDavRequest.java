package com.tazamon.dav.web;

import java.util.Optional;

/**
 * The goal of this {@link WebDavRequest} is to wrap the boilerplate code performing Web Dav basic operations.
 */
public interface WebDavRequest {

    /**
     * This method is to wrap the Web Dav request and simplify building your request and parsing your response.
     *
     * @param requestWrapper {@link RequestWrapper} entity representing the requirement for performing Web Dav Request.
     * @return {@link DavResponse} instance used to get the desired data from the response.
     */
    Optional<DavResponse> submitRequest(RequestWrapper requestWrapper);
}
