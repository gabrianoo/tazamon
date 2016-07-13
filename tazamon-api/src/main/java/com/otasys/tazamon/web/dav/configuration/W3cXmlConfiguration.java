package com.otasys.tazamon.web.dav.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

@Configuration
public class W3cXmlConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(W3cXmlConfiguration.class);

    @Bean
    public TransformerFactory provideTransformerFactory() {
        return TransformerFactory.newInstance();
    }

    @Bean
    public Transformer provideTransformer(TransformerFactory transformerFactory) throws TransformerConfigurationException {
        try {
            return transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            LOGGER.error("Error creating transformer from [{}]", TransformerFactory.class.getCanonicalName());
            throw new TransformerConfigurationException(e);
        }
    }
}
