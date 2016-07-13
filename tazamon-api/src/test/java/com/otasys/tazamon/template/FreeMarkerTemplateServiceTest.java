package com.otasys.tazamon.template;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class FreeMarkerTemplateServiceTest {

    private Template template;
    private FreeMarkerTemplateService freeMarkerTemplateService;

    @Before
    public void setUp() {
        template = mock(Template.class);
        freeMarkerTemplateService = new FreeMarkerTemplateService();
    }

    @Test
    public void givenFreeMarkerTemplateServiceAndTemplateWhenProcessTemplateIntoStringThenTemplateStringShouldReturn() throws IOException, TemplateException {
        final String expectedResult = "";
        doNothing().when(template).process(any(Object.class), any(StringWriter.class));
        assertThat(
                freeMarkerTemplateService.processTemplateIntoString(template, null),
                is(equalTo(expectedResult))
        );
    }
}