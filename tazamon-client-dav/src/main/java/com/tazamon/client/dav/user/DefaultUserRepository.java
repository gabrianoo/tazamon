package com.tazamon.client.dav.user;

import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonExecutor;
import com.tazamon.client.dav.common.DefaultDavTazamonRequest;
import com.tazamon.client.dav.xml.CurrentUserPrincipal;
import com.tazamon.client.dav.xml.Property;
import com.tazamon.client.dav.xml.PropertyFind;
import com.tazamon.common.ServerProperties;
import com.tazamon.user.User;
import com.tazamon.user.UserRepository;
import com.tazamon.xml.XmlProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
@Slf4j
public class DefaultUserRepository implements UserRepository {

    private final DavTazamonExecutor davTazamonExecutor;
    private final XmlProcessor xmlProcessor;
    private final DavTazamonAdapter<Optional<User>> userDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public DefaultUserRepository(
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
    public Optional<User> findUser(String login, String password) {
        PropertyFind propFind = new PropertyFind(
                new Property(
                        new CurrentUserPrincipal()
                )
        );
        return xmlProcessor.toXml(propFind)
                .map(document ->
                        new DefaultDavTazamonRequest(login, password, document, serverProperties.getCalendarServer())
                )
                .flatMap(davTazamonExecutor::execute)
                .flatMap(userDavTazamonAdapter::adapt);
    }
}
