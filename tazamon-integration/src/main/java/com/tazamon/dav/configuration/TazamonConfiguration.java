package com.tazamon.dav.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * General Spring Configuration class to cover all the beans that are manually manged by our project.
 */
@Configuration
public class TazamonConfiguration {

    /**
     * Build the JAXB2 Marshaller service that is used by Spring OXM to abstract working with XML using standard java
     * {@link Marshaller} and {@link Unmarshaller}.
     *
     * @return {@link Jaxb2Marshaller} instance.
     */
    @Bean
    public Jaxb2Marshaller provideJaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("com.tazamon.dav");
        return jaxb2Marshaller;
    }

    /**
     * Spring JAXB2 {@link Marshaller} service implementation.
     *
     * @param jaxb2Marshaller service used by Spring OXM for abstracting XML {@link Marshaller}/{@link Unmarshaller}.
     * @return {@link Marshaller} instance.
     * @throws JAXBException if an error was encountered while creating the {@link Marshaller} object.
     */
    @Bean
    public Unmarshaller provideUnmarshaller(Jaxb2Marshaller jaxb2Marshaller) throws JAXBException {
        return jaxb2Marshaller.getJaxbContext().createUnmarshaller();
    }

    /**
     * Spring JAXB2 {@link Unmarshaller} service implementation.
     *
     * @param jaxb2Marshaller service used by Spring OXM for abstracting XML {@link Marshaller}/{@link Unmarshaller}.
     * @return {@link Unmarshaller} instance.
     * @throws JAXBException if an error was encountered while creating the {@link Unmarshaller} object.
     */
    @Bean
    public Marshaller provideMarshaller(Jaxb2Marshaller jaxb2Marshaller) throws JAXBException {
        return jaxb2Marshaller.getJaxbContext().createMarshaller();
    }
}
