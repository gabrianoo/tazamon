package com.tazamon.dav.common;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Optional;

/**
 * This is an extra layer of abstraction (I don't know whether it is needed or no) to wrap the {@link Marshaller} and
 * the {@link Unmarshaller}.
 * The final goal is to avoid working with XML Documents.
 */
@Slf4j
@Named
public class XmlProcessor {

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    @Inject
    public XmlProcessor(Marshaller marshaller, Unmarshaller unmarshaller) {
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }

    /**
     * Convert the provided entity to the corresponding XML.
     *
     * @param t   the entity to be converted to the desired XML.
     * @param <T> the generic data type of the converted entity.
     * @return an {@link Optional} XML {@link Document} representing the desired output.
     */
    public <T> Optional<Document> toXml(T t) {
        Optional<Document> nodeOptional = Optional.empty();
        try {
            Document xmlNode = buildXmlNode();
            marshaller.marshal(t, xmlNode);
            nodeOptional = Optional.of(xmlNode);
        } catch (JAXBException | ParserConfigurationException e) {
            log.error("", e);
        }
        return nodeOptional;
    }

    /**
     * Convert the provided XML to the desired entity.
     *
     * @param node         to be converted to the required entity.
     * @param declaredType the type of the required entity.
     * @param <T>          the generic data type of the required entity.
     * @return an {@link Optional} entity representing the desired output.
     */
    public <T> Optional<T> fromXml(Node node, Class<T> declaredType) {
        Optional<T> typeOptional = Optional.empty();
        try {
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(node, declaredType);
            typeOptional = Optional.ofNullable(jaxbElement.getValue());
        } catch (JAXBException e) {
            log.error("", e);
        }
        return typeOptional;
    }

    /**
     * A helper method for building an empty XML {@link Document}.
     *
     * @return an empty XML {@link Document}.
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration
     *                                      requested.
     */
    public static Document buildXmlNode() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }
}
