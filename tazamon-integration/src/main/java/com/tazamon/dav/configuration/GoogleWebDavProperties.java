package com.tazamon.dav.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.inject.Named;

/**
 * Google general web dav properties.
 */
@Data
@Named
@ConfigurationProperties(prefix = "web.dav.google")
public class GoogleWebDavProperties {

    private String calendarServer;
    private String cardServer;
}
