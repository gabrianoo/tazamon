package com.otasys.tazamon.web.dav.configuration;

import com.otasys.tazamon.web.dav.DefaultWebDavService;
import com.otasys.tazamon.web.dav.WebDavService;
import com.otasys.tazamon.xml.DefaultXmlConversionService;
import com.otasys.tazamon.xml.XmlConversionService;
import org.apache.commons.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

@Configuration
public class TazamonConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TazamonConfiguration.class);

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("tazamon.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }

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

    @Bean
    public HttpClient provideHttpClient() {
        return new HttpClient();
    }

    @Bean
    public XmlConversionService provideXmlConversionService(Transformer transformer) {
        return new DefaultXmlConversionService(transformer);
    }

    @Bean
    public WebDavService provideWebDavService(
            HttpClient httpClient,
            freemarker.template.Configuration freeMarkerConfiguration,
            XmlConversionService xmlConversionService
    ) {
        return new DefaultWebDavService(httpClient, freeMarkerConfiguration, xmlConversionService);
    }
}
