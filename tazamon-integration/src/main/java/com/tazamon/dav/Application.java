package com.tazamon.dav;

import com.tazamon.dav.web.apple.AppleWebDavFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Inject
    private AppleWebDavFactory appleWebDavFactory;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        appleWebDavFactory
                .provideUser("eng.ahmedgaber@gmail.com", "qata-jnlt-lojj-evkx")
                .ifPresent(user -> log.info("{}", user));
    }
}
