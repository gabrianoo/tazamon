package com.gability.tazamon.client;

import java.util.Optional;

/**
 * This is an extra api layer to support processing messages from different systems formats.
 */
public interface Processor {

    /**
     * Convert the provided entity to the corresponding {@link String} payload.
     *
     * @param payload the entity to be converted to the desired payload.
     * @param <P>     the generic data type of the converted payload.
     * @return an {@link Optional}<{@link String}> representing the desired output.
     */
    <P> Optional<String> to(P payload);

    /**
     * Convert the provided payload to the desired entity.
     *
     * @param payload      to be converted to the required payload data type.
     * @param declaredType the type of the required payload.
     * @param <T>          the generic data type of the required entity.
     * @return an entity representing the desired payload output.
     */
    <T> Optional<T> from(String payload, Class<T> declaredType);
}
