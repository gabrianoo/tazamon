package com.otasys.tazamon.xml;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class DefaultXmlConversionServiceTest {

    private XmlConversionService xmlConversionService;
    private Document xmlDocument;
    private String xmlString;
    private String xmlXPath;

    @Before
    public void setupTest() throws ParserConfigurationException, TransformerConfigurationException {
        // Initialize Service To Test
        xmlConversionService = new DefaultXmlConversionService(TransformerFactory.newInstance().newTransformer());
        // Initialize XML Document for verification.
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        xmlDocument = documentBuilder.newDocument();
        xmlDocument.setXmlStandalone(true);
        Element rootElement = xmlDocument.createElement("propfind");
        xmlDocument.appendChild(rootElement);
        Element propElement = xmlDocument.createElement("prop");
        rootElement.appendChild(propElement);
        Element currentUserPrincipalElement = xmlDocument.createElement("current-user-principal");
        propElement.appendChild(currentUserPrincipalElement);
        // Initialize XML String for verification.
        xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><propfind><prop><current-user-principal/></prop></propfind>";
        // Initalize the XML XPath
        xmlXPath = "/propfind/prop/current-user-principal";
    }

    @Test
    public void givenValidXmlDocumentWhenLoadXmlDocumentFromStringThenStringShouldMatchDocument() throws Exception {
        String actualXmlString = xmlConversionService.loadStringFromXmlDocument(xmlDocument);
        assertThat(actualXmlString, is(equalTo(xmlString)));
    }

    @Test
    public void givenValidXmlDocumentWhenLoadXmlDocumentFromStringAndTransformationFailedThenEmptyStringShouldReturn()
            throws TransformerException {
        Transformer mockTransformer = mock(Transformer.class);
        doThrow(TransformerException.class)
                .when(mockTransformer)
                .transform(any(Source.class), any(Result.class));
        XmlConversionService mockXmlConversionService = new DefaultXmlConversionService(mockTransformer);
        String actualXmlString = mockXmlConversionService.loadStringFromXmlDocument(xmlDocument);
        assertThat(actualXmlString, isEmptyString());
    }

    @Test
    public void givenXmlStringWhenLoadStringFromXmlDocumentThenDocumentShouldMatchString() throws Exception {
        Document actualXmlDocument = xmlConversionService.loadXmlDocumentFromString(xmlString);
        assertThat(actualXmlDocument, hasXPath(xmlXPath));
    }

    @Test
    public void givenValidXmlDocumentWhenLoadStringFromXmlDocumentAndTransformationFailedThenEmptyDocumentShouldReturn()
            throws TransformerException {
        Transformer mockTransformer = mock(Transformer.class);
        doThrow(TransformerException.class)
                .when(mockTransformer)
                .transform(any(Source.class), any(Result.class));
        XmlConversionService mockXmlConversionService = new DefaultXmlConversionService(mockTransformer);
        Document actualXmlDocument = mockXmlConversionService.loadXmlDocumentFromString(xmlString);
        assertThat(actualXmlDocument, is(nullValue()));
    }
}