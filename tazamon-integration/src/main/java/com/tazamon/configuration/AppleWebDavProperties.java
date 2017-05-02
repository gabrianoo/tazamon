package com.tazamon.configuration;

import com.tazamon.common.ServerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.inject.Named;

/**
 * Apple general web dav properties.
 */
@Data
@Named
@ConfigurationProperties(prefix = "web.dav.apple")
public class AppleWebDavProperties implements ServerProperties {

    private String calendarServer;
    private String cardServer;
}
