package com.otasys.tazamon.xml;

import org.w3c.dom.Document;

public interface XmlConversionService {

    Document loadXmlDocumentFromString(String xml);

    String loadStringFromXmlDocument(Document xml);
}
