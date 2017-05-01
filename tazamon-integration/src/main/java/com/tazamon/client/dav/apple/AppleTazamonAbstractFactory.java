package com.tazamon.client.dav.apple;

import com.tazamon.client.dav.DefaultDavTazamonRequest;
import com.tazamon.common.TazamonAbstractFactory;
import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.common.FreeMarkerContentProducer;
import com.tazamon.common.User;
import com.tazamon.configuration.AppleWebDavProperties;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
@Slf4j
public class AppleTazamonAbstractFactory implements TazamonAbstractFactory {

    private final DavTazamonExecutor davTazamonExecutor;
    private final FreeMarkerContentProducer freeMarkerContentProducer;
    private final DavTazamonAdapter<Optional<User>> userDavTazamonAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleTazamonAbstractFactory(
            DavTazamonExecutor davTazamonExecutor,
            FreeMarkerContentProducer freeMarkerContentProducer,
            DavTazamonAdapter<Optional<User>> userDavTazamonAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.freeMarkerContentProducer = freeMarkerContentProducer;
        this.userDavTazamonAdapter = userDavTazamonAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    @Override
    public Optional<User> provideUser(String email, String password) {
        return freeMarkerContentProducer
                .processTemplateIntoOptionalString(appleWebDavProperties.getFtl().getCurrentUserPrincipal(), null)
                .map(document ->
                        new DefaultDavTazamonRequest(email, password, document, appleWebDavProperties.getCardServer())
                )
                .flatMap(davTazamonExecutor::execute)
                .flatMap(userDavTazamonAdapter::adapt);
    }
}
