package com.tazamon.client.dav.apple;

import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.common.DefaultDavTazamonRequest;
import com.tazamon.client.dav.common.XmlProcessor;
import com.tazamon.client.dav.xml.CurrentUserPrincipal;
import com.tazamon.client.dav.xml.ETag;
import com.tazamon.client.dav.xml.Property;
import com.tazamon.client.dav.xml.PropertyFind;
import com.tazamon.common.ServerProperties;
import com.tazamon.common.TazamonAbstractFactory;
import com.tazamon.common.User;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
@Slf4j
public class AppleTazamonAbstractFactory implements TazamonAbstractFactory {

    private final DavTazamonExecutor davTazamonExecutor;
    private final XmlProcessor xmlProcessor;
    private final DavTazamonAdapter<Optional<User>> userDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public AppleTazamonAbstractFactory(
            DavTazamonExecutor davTazamonExecutor,
            XmlProcessor xmlProcessor,
            DavTazamonAdapter<Optional<User>> userDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.xmlProcessor = xmlProcessor;
        this.userDavTazamonAdapter = userDavTazamonAdapter;
        this.serverProperties = serverProperties;
    }

    @Override
    public Optional<User> provideUser(String email, String password) {
        CurrentUserPrincipal currentUserPrincipal = new CurrentUserPrincipal();
        Property prop = new Property(currentUserPrincipal);
        PropertyFind propFind = new PropertyFind(prop);
        return xmlProcessor.toXml(propFind)
                .map(document ->
                        new DefaultDavTazamonRequest(email, password, document, serverProperties.getCalendarServer())
                )
                .flatMap(davTazamonExecutor::execute)
                .flatMap(userDavTazamonAdapter::adapt);
    }
}
