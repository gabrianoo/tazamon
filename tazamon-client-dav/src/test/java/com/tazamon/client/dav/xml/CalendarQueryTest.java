package com.tazamon.client.dav.xml;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static net.fortuna.ical4j.model.Calendar.VCALENDAR;
import static net.fortuna.ical4j.model.Component.VEVENT;
import static org.assertj.core.api.Assertions.assertThat;

public class CalendarQueryTest {

    private static final String PAYLOAD = "CalendarQuery.xml";
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;

    @Before
    public void setup() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CalendarQuery.class, ETag.class, CalendarData.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    @Test
    public void givenCalendarQueryXmlDocumentWhenUnMarshallingItThenItIsCorrectlyParsed() throws JAXBException {
        CalendarQuery calendarQuery = (CalendarQuery) jaxbUnmarshaller.unmarshal(getClass().getResourceAsStream(PAYLOAD));
        assertThat(calendarQuery).isEqualTo(buildCalendarQuery());
    }

    @Test
    public void givenCalendarQueryEntityWhenMarshallingItThenItIsCorrectlyParsed() throws JAXBException {
        ByteArrayOutputStream actualOutputStream = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(buildCalendarQuery(), actualOutputStream);
        assertThat(actualOutputStream.toString())
                .contains(VCALENDAR)
                .contains(VEVENT);
    }

    private CalendarQuery buildCalendarQuery() {
        return new CalendarQuery(
                Arrays.asList(
                        new ETag(""),
                        new CalendarData("")
                ),
                new Filter(
                        new CompFilter(
                                VCALENDAR,
                                new CompFilter(VEVENT, null)
                        )
                )
        );
    }
}
