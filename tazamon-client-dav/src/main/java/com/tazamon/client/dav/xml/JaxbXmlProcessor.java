package com.tazamon.client.dav.xml;

import com.tazamon.xml.XmlProcessor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;
import java.util.Optional;

/**
 * JAXB implementation for processing XML to support {@link Marshaller} and the {@link Unmarshaller} XML messages.
 */
@Slf4j
@Named
public class JaxbXmlProcessor implements XmlProcessor {

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    @Inject
    public JaxbXmlProcessor(Marshaller marshaller, Unmarshaller unmarshaller) {
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }

    @Override
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

    @Override
    public <T> Optional<T> fromXml(Node node, Class<T> declaredType) {
        Optional<T> optionalT = Optional.empty();
        try {
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(node, declaredType);
            optionalT = Optional.of(jaxbElement.getValue());
        } catch (JAXBException e) {
            log.error("", e);
        }
        return optionalT;
    }
}
