package com.tazamon.client.http.apple;

import com.tazamon.client.http.DefaultHttpTazamonRequest;
import com.tazamon.client.http.HttpTazamonAbstractFactory;
import com.tazamon.client.http.HttpTazamonAdapter;
import com.tazamon.client.http.HttpTazamonRequester;
import com.tazamon.common.FreeMarkerContentProducer;
import com.tazamon.common.User;
import com.tazamon.configuration.AppleWebDavProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
@Slf4j
public class AppleHttpTazamonAbstractFactory implements HttpTazamonAbstractFactory {

    private final HttpTazamonRequester httpTazamonRequester;
    private final FreeMarkerContentProducer freeMarkerContentProducer;
    private final HttpTazamonAdapter<Optional<User>, DavPropertySet> userHttpTazamonAdapter;
    private final AppleWebDavProperties appleWebDavProperties;

    @Inject
    public AppleHttpTazamonAbstractFactory(
            HttpTazamonRequester httpTazamonRequester,
            FreeMarkerContentProducer freeMarkerContentProducer,
            HttpTazamonAdapter<Optional<User>, DavPropertySet> userHttpTazamonAdapter,
            AppleWebDavProperties appleWebDavProperties
    ) {
        this.httpTazamonRequester = httpTazamonRequester;
        this.freeMarkerContentProducer = freeMarkerContentProducer;
        this.userHttpTazamonAdapter = userHttpTazamonAdapter;
        this.appleWebDavProperties = appleWebDavProperties;
    }

    @Override
    public Optional<User> provideUser(String email, String password) {
        return freeMarkerContentProducer
                .processTemplateIntoOptionalString(appleWebDavProperties.getFtl().getCurrentUserPrincipal(), null)
                .map(document ->
                        new DefaultHttpTazamonRequest(email, password, document, appleWebDavProperties.getCardServer())
                )
                .flatMap(httpTazamonRequester::submitRequest)
                .flatMap(userHttpTazamonAdapter::adapt);
    }
}
