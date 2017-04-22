package com.tazamon.dav.web;

import java.util.Optional;

/**
 * Adapter interface for adapting the {@link DavResponse} to a meaningful entity object.
 *
 * @param <T> The target data type required for adaptation.
 */
public interface DavResponseAdapter<T> {

    /**
     * Convert the {@link DavResponse} to meaningful entity object.
     *
     * @param davResponse {@link DavResponse} to be used in conversion.
     * @return The required entity object after conversion as an {@link Optional}.
     */
    Optional<T> adapt(DavResponse davResponse);
}
