package com.tazamon.xml;

import org.w3c.dom.Node;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Optional;

/**
 * This is an extra api layer to support {@link Marshaller} and the {@link Unmarshaller} XML messages.
 */
public interface XmlProcessor {

    /**
     * Convert the provided entity to the corresponding {@link String} XML.
     *
     * @param t   the entity to be converted to the desired XML.
     * @param <T> the generic data type of the converted entity.
     * @return an {@link Optional} XML {@link String} representing the desired output.
     */
    <T> Optional<String> toXml(T t);

    /**
     * Convert the provided XML to the desired entity.
     *
     * @param node         to be converted to the required entity.
     * @param declaredType the type of the required entity.
     * @param <T>          the generic data type of the required entity.
     * @return an entity representing the desired output.
     */
    <T> Optional<T> fromXml(Node node, Class<T> declaredType);
}
