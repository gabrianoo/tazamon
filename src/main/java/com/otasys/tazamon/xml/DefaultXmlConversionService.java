package com.otasys.tazamon.xml;

import com.otasys.tazamon.web.dav.DefaultWebDavService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Named
public class DefaultXmlConversionService implements XmlConversionService {

    private Transformer transformer;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebDavService.class);

    @Inject
    public DefaultXmlConversionService(Transformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public Document loadXmlDocumentFromString(String xml) {
        StreamSource source = new StreamSource(new StringReader(xml));
        DOMResult result = new DOMResult();
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            LOGGER.error("Error converting the following string [{}] to XML [{}]", xml, Document.class.getCanonicalName());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("", e);
            }
        }
        return (Document) result.getNode();
    }

    @Override
    public String loadStringFromXmlDocument(Document document) {
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new StringWriter());
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            LOGGER.error("Error converting the following XML document [{}] to String [{}]", document, Document.class.getCanonicalName());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("", e);
            }
        }
        return result.getWriter().toString();
    }

}
