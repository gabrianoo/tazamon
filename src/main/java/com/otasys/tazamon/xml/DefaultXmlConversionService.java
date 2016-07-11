package com.otasys.tazamon.xml;

import com.otasys.tazamon.web.dav.DefaultWebDavService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.inject.Named;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Named
public class DefaultXmlConversionService implements XmlConversionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    @Override
    public Document loadXmlDocumentFromString(String xml) {
        Source source = new StreamSource(new StringReader(xml));
        DOMResult result = new DOMResult();
        try {
            TransformerFactory.newInstance().newTransformer().transform(source, result);
        } catch (TransformerException e) {
            LOGGER.error("Error converting the following string [{}] to XML [{}]", xml, Document.class.getCanonicalName());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("", e);
            }
        }
        return (Document) result.getNode();
    }
}
