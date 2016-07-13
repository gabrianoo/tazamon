package com.otasys.tazamon.web.dav.configuration;

import com.otasys.tazamon.GeneralTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GeneralTestConfiguration.class)
public class WebDavICloudPropertiesTest {

    @Inject
    private WebDavICloudProperties webDavICloudProperties;

    @Test
    public void givenApplicationYmlWhenSpringBootLoadsFileThenAllICloudPropertiesShouldBeThere() {
        assertThat(webDavICloudProperties, is(notNullValue()));
    }

    @Test
    public void givenApplicationYmlWhenSpringBootFileThenCardDavICloudPropertiesShouldBeThere() {
        assertThat(
                webDavICloudProperties.getCardServers(),
                hasItem("contacts.icloud.com")
        );
    }

    @Test
    public void givenApplicationYmlWhenSpringBootFileThenCalDavICloudPropertiesShouldBeThere() {
        assertThat(
                webDavICloudProperties.getCalendarServers(),
                hasItem("caldav.icloud.com")
        );
    }
}