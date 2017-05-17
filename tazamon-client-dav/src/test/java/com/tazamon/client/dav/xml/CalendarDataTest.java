package com.tazamon.client.dav.xml;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static com.tazamon.client.dav.common.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class CalendarDataTest {

    private static final String PAYLOAD = "CalendarData.xml";
    private static final String EXPECTED_HREF = "/123456789.ics";
    private static final String EXPECTED_CALENDAR_DATA = "VCALENDAR";
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
    public void givenCalendarDataXmlDocumentWhenUnMarshallingItThenItIsCorrectlyParsed() throws JAXBException {
        MultiStatus multiStatus = (MultiStatus) jaxbUnmarshaller.unmarshal(getClass().getResourceAsStream(PAYLOAD));
        assertThat(multiStatus).isEqualTo(buildMultiStatus());
    }

    @Test
    public void givenCalendarDataEntityWhenMarshallingItThenItIsCorrectlyParsed() throws JAXBException {
        ByteArrayOutputStream actualOutputStream = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(buildMultiStatus(), actualOutputStream);
        assertThat(actualOutputStream.toString())
                .contains(EXPECTED_HREF)
                .contains(OK.getStatus())
                .contains(EXPECTED_CALENDAR_DATA);
    }

    private MultiStatus buildMultiStatus() {
        return new MultiStatus(
                Collections.singletonList(
                        new Response(
                                EXPECTED_HREF,
                                new PropertyStatus(
                                        new Property(
                                                new CalendarData(
                                                        EXPECTED_CALENDAR_DATA
                                                )
                                        ),
                                        OK.getStatus()
                                )
                        )
                )
        );
    }
}
