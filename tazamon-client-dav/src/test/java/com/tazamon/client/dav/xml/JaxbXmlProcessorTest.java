package com.tazamon.client.dav.xml;

import com.tazamon.xml.XmlProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class JaxbXmlProcessorTest {

    private static final String EMPTY_STRING = "";
    @Mock
    private Marshaller marshaller;
    @Mock
    private Unmarshaller unmarshaller;
    @Mock
    private Node node;
    @Mock
    private JAXBElement<Object> objectJAXBElement;
    private XmlProcessor underTestJaxbXmlProcessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTestJaxbXmlProcessor = new JaxbXmlProcessor(marshaller, unmarshaller);
    }

    @Test
    public void givenValidEntityObjectWhenMarshalingThenNotEmptyIsReturned() throws JAXBException {
        doNothing().when(marshaller)
                .marshal(
                        anyObject(), any(Writer.class)
                );
        assertThat(
                underTestJaxbXmlProcessor.toXml(new Object())
        ).contains(EMPTY_STRING);
    }

    @Test
    public void givenInvalidEntityObjectWhenMarshalingThenAnEmptyOptionalIsReturned() throws JAXBException {
        doThrow(JAXBException.class).when(marshaller)
                .marshal(
                        anyObject(), any(Writer.class)
                );
        assertThat(
                underTestJaxbXmlProcessor.toXml(new Object())
        ).isNotPresent();
    }

    @Test
    public void givenValidNodeAndClassTypeWhenUnMarshalingThenNotEmptyIsReturned() throws JAXBException {
        Object o = new Object();
        doReturn(o).when(objectJAXBElement)
                .getValue();
        doReturn(objectJAXBElement).when(unmarshaller)
                .unmarshal(
                        any(Node.class), any()
                );
        assertThat(
                underTestJaxbXmlProcessor.fromXml(node, Object.class)
        ).contains(o);
    }

    @Test
    public void givenInvalidNodeOrClassTypeWhenUnMarshalingThenAnEmptyOptionalIsReturned() throws JAXBException {
        doThrow(JAXBException.class).when(unmarshaller)
                .unmarshal(
                        any(Node.class), any()
                );
        assertThat(
                underTestJaxbXmlProcessor.fromXml(node, Object.class)
        ).isNotPresent();
    }
}