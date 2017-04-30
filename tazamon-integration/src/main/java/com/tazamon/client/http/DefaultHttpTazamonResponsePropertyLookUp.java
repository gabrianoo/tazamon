package com.tazamon.client.http;


import com.tazamon.common.XmlProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.w3c.dom.Element;

import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Optional;

@Slf4j
@Named
public class DefaultHttpTazamonResponsePropertyLookUp
        implements HttpTazamonResponsePropertyLookUp<DavPropertySet> {

    @Override
    public Optional<Element> lookUpProperty(
            HttpTazamonResponse<DavPropertySet> httpTazamonResponse,
            String propertyName
    ) {
        DavProperty<?> davProperty = httpTazamonResponse.getPropertySet().get(propertyName);
        Optional<Element> optionalDocument = Optional.empty();
        try {
            if (davProperty != null) {
                optionalDocument = Optional.of(davProperty.toXml(XmlProcessor.buildXmlNode()));
            }
        } catch (ParserConfigurationException e) {
            log.error("", e);
        }
        return optionalDocument;
    }
}
