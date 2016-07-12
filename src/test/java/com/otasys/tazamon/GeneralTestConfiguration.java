package com.otasys.tazamon;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.otasys.tazamon.web.dav", "com.otasys.tazamon.xml"})
public class GeneralTestConfiguration {
}
