package com.tazamon.client.dav.common;

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
import java.io.StringWriter;
import java.util.Optional;

/**
 * This is an extra layer of api (I don't know whether it is needed or no) to wrap the {@link Marshaller} and
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
     * @return an {@link Optional} XML {@link String} representing the desired output.
     */
    public <T> Optional<String> toXml(T t) {
        Optional<String> nodeOptional = Optional.empty();
        try {
            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(t, xmlWriter);
            nodeOptional = Optional.of(xmlWriter.toString());
        } catch (JAXBException e) {
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
     * @return an entity representing the desired output.
     * @throws JAXBException if the parsing failed for any reason.
     */
    public <T> T fromXml(Node node, Class<T> declaredType) throws JAXBException {
        JAXBElement<T> jaxbElement = unmarshaller.unmarshal(node, declaredType);
        return jaxbElement.getValue();
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
