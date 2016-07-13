package com.otasys.tazamon;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@EnableAutoConfiguration
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@ComponentScan(basePackages = {"com.otasys.tazamon.web.dav", "com.otasys.tazamon.xml"})
public class GeneralTestConfiguration {
}
