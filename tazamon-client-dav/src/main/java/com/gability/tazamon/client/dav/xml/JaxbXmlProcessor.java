package com.gability.tazamon.client.dav.xml;

import com.gability.tazamon.client.Processor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

/**
 * JAXB implementation for processing XML to support {@link Marshaller} and the {@link Unmarshaller} XML messages.
 */
@Slf4j
@Named
public class JaxbXmlProcessor implements Processor {

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    @Inject
    public JaxbXmlProcessor(Marshaller marshaller, Unmarshaller unmarshaller) {
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }

    @Override
    public <T> Optional<String> to(T t) {
        Optional<String> nodeOptional = Optional.empty();
        try (StringWriter xmlWriter = new StringWriter()) {
            marshaller.marshal(t, xmlWriter);
            nodeOptional = Optional.of(xmlWriter.toString());
        } catch (JAXBException | IOException e) {
            log.error(e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
        }
        return nodeOptional;
    }

    @Override
    public <T> Optional<T> from(String xml, Class<T> declaredType) {
        Optional<T> optionalT = Optional.empty();
        try {
            StreamSource streamSource = new StreamSource(new StringReader(xml));
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(streamSource, declaredType);
            optionalT = Optional.of(jaxbElement.getValue());
        } catch (JAXBException e) {
            log.error(e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
        }
        return optionalT;
    }
}
