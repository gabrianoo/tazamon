package com.otasys.tazamon.web.dav.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.inject.Named;
import java.util.List;

@Named
@ConfigurationProperties(prefix = "web.dav.icloud")
public class WebDavICloudProperties {

    private List<String> calendarServers;
    private List<String> cardServers;

    public List<String> getCalendarServers() {
        return calendarServers;
    }

    public void setCalendarServers(List<String> calendarServers) {
        this.calendarServers = calendarServers;
    }

    public List<String> getCardServers() {
        return cardServers;
    }

    public void setCardServers(List<String> cardServers) {
        this.cardServers = cardServers;
    }
}
