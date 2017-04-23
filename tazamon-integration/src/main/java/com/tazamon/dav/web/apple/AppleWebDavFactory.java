package com.tazamon.dav.web.apple;

import com.tazamon.dav.common.FreeMarkerContentProducer;
import com.tazamon.dav.configuration.AppleWebDavProperties;
import com.tazamon.dav.web.*;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
@Slf4j
public class AppleWebDavFactory implements WebDavFactory {

    private final WebDavRequest webDavRequest;
    private final FreeMarkerContentProducer freeMarkerContentProducer;
    private final DavResponseAdapter<User> userDavResponseAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleWebDavFactory(
            WebDavRequest webDavRequest,
            FreeMarkerContentProducer freeMarkerContentProducer,
            DavResponseAdapter<User> userDavResponseAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.webDavRequest = webDavRequest;
        this.freeMarkerContentProducer = freeMarkerContentProducer;
        this.userDavResponseAdapter = userDavResponseAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    @Override
    public Optional<User> provideUser(String email, String password) {
        return freeMarkerContentProducer
                .processTemplateIntoOptionalString(appleWebDavProperties.getFtl().getCurrentUserPrincipal(), null)
                .flatMap(document -> Optional.of(
                        RequestWrapper.builder()
                                .base64EncodeAuthToken(email, password)
                                .httpEntity(document)
                                .serverUrl(appleWebDavProperties.getCardServer())
                                .build()
                ))
                .flatMap(webDavRequest::submitRequest)
                .flatMap(userDavResponseAdapter::adapt);
    }
}
