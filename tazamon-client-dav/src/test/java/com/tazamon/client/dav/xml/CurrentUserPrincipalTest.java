package com.tazamon.client.dav.xml;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static com.tazamon.client.dav.common.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class CurrentUserPrincipalTest {

    private static final String PAYLOAD = "CurrentUserPrincipal.xml";
    private static final String EXPECTED_HREF = "/";
    private static final String EXPECTED_CURRENT_USER_PRINCIPAL = "/123456789/principal/";
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;

    @Before
    public void setup() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MultiStatus.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    @Test
    public void givenCurrentUserPrincipalXmlDocumentWhenUnMarshallingItThenItIsCorrectlyParsed() throws JAXBException {
        MultiStatus multiStatus = (MultiStatus) jaxbUnmarshaller.unmarshal(getClass().getResourceAsStream(PAYLOAD));
        assertThat(multiStatus).isEqualTo(buildCurrentUserPrincipal());
    }

    @Test
    public void givenCurrentUserPrincipalEntityWhenMarshallingItThenItIsCorrectlyParsed() throws JAXBException {
        ByteArrayOutputStream actualOutputStream = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(buildCurrentUserPrincipal(), actualOutputStream);
        assertThat(actualOutputStream.toString())
                .contains(EXPECTED_HREF)
                .contains(OK.getStatus())
                .contains(EXPECTED_CURRENT_USER_PRINCIPAL);
    }

    private MultiStatus buildCurrentUserPrincipal() {
        return new MultiStatus(
                Collections.singletonList(
                        new Response(
                                EXPECTED_HREF,
                                new PropertyStatus(
                                        new Property(
                                                new CurrentUserPrincipal(
                                                        EXPECTED_CURRENT_USER_PRINCIPAL
                                                )
                                        ),
                                        OK.getStatus()
                                )
                        )
                )
        );
    }
}
