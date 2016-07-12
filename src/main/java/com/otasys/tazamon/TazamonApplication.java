package com.otasys.tazamon;

import com.otasys.tazamon.web.dav.WebDavService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;

@SpringBootApplication
public class TazamonApplication implements CommandLineRunner {

    @Inject
    private WebDavService webDavService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TazamonApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TazamonApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.info("Server support WEB DAV {}", webDavService.isWebDavSupported());
    }

}
