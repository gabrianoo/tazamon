package com.tazamon.dav.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Named
public class FreeMarkerContentProducer {

    private final Configuration configuration;

    @Inject
    public FreeMarkerContentProducer(Configuration configuration) {
        this.configuration = configuration;
    }

    public Optional<String> processTemplateIntoOptionalString(String template, Object data) {
        Optional<String> stringOptional = Optional.empty();
        try {
            Template t = configuration.getTemplate(template);
            stringOptional = Optional.of(FreeMarkerTemplateUtils.processTemplateIntoString(t, data));
        } catch (TemplateException | IOException e) {
            log.error("", e);
        }
        return stringOptional;
    }
}
