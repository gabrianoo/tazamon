package com.tazamon.dav.web.apple;

import com.tazamon.dav.common.XmlProcessor;
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
    private final XmlProcessor xmlProcessor;
    private final DavResponseAdapter<User> userDavResponseAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleWebDavFactory(
            WebDavRequest webDavRequest,
            XmlProcessor xmlProcessor,
            DavResponseAdapter<User> userDavResponseAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.webDavRequest = webDavRequest;
        this.xmlProcessor = xmlProcessor;
        this.userDavResponseAdapter = userDavResponseAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    private PropFind buildPropFind() {
        PropFind propFind = new PropFind();
        Prop prop = new Prop();
        prop.setCurrentUserPrincipal(null);
        propFind.setProp(prop);
        return propFind;
    }

    @Override
    public Optional<User> provideUser(String email, String password) {
        return xmlProcessor.toXml(buildPropFind())
                .flatMap(document -> Optional.of(
                        RequestWrapper.builder()
                                .base64EncodeAuthToken(email, password)
                                .requestEntity(document)
                                .serverUrl(appleWebDavProperties.getCardServer())
                                .build()
                        )
                )
                .flatMap(webDavRequest::submitRequest)
                .flatMap(userDavResponseAdapter::adapt);
    }
}
