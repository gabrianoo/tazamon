package com.tazamon.dav.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.inject.Named;

/**
 * Apple general web dav properties.
 */
@Data
@Named
@ConfigurationProperties(prefix = "web.dav.apple")
public class AppleWebDavProperties {

    private String calendarServer;
    private String cardServer;
    private Ftl ftl;

    @Data
    public static class Ftl {

        private String currentUserPrincipal;
        private String displayName;
    }
}
