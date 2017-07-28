package com.gability.tazamon.client.dav;

/**
 * Adapter interface for adapting the {@link DavTazamonResponse} to a meaningful entity object.
 *
 * @param <T> The target data type required for adaptation.
 */
public interface DavTazamonAdapter<T> {

    /**
     * Convert the {@link DavTazamonResponse} to meaningful entity object.
     *
     * @param tazamonResponse {@link DavTazamonResponse} to be used in conversion.
     * @return The required entity object after conversion.
     */
    T adapt(DavTazamonResponse tazamonResponse);
}
