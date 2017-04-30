package com.tazamon.client.http;

/**
 * Adapter interface for adapting the {@link HttpTazamonResponse#getPropertySet()} to a meaningful entity object.
 *
 * @param <T> The target data type required for adaptation.
 */
public interface HttpTazamonAdapter<T, R> {

    /**
     * Convert the {@link HttpTazamonResponse#getPropertySet()} to meaningful entity object.
     *
     * @param tazamonResponse {@link HttpTazamonResponse} to be used in conversion.
     * @return The required entity object after conversion.
     */
    T adapt(HttpTazamonResponse<R> tazamonResponse);
}
