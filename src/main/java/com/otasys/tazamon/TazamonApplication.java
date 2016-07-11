package com.otasys.tazamon;

import com.otasys.tazamon.web.dav.WebDavService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;
import java.nio.charset.Charset;

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
        String auth = strings[0] + ":" + strings[1];
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        LOGGER.info("Server support WEB DAV {}", webDavService.isWebDavSupported());
        webDavService.getUserPrincipalUnifiedId(authHeader);
    }

}
