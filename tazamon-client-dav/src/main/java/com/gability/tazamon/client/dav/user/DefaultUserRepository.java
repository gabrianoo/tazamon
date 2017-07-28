package com.gability.tazamon.client.dav.user;

import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DefaultDavTazamonRequest;
import com.gability.tazamon.client.dav.xml.CurrentUserPrincipal;
import com.gability.tazamon.client.dav.xml.Property;
import com.gability.tazamon.client.dav.xml.PropertyFind;
import com.gability.tazamon.configuration.ServerProperties;
import com.gability.tazamon.user.User;
import com.gability.tazamon.user.UserRepository;
import com.gability.tazamon.client.Processor;
import com.gability.tazamon.client.dav.DavTazamonExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
@Slf4j
public class DefaultUserRepository implements UserRepository {

    private final DavTazamonExecutor davTazamonExecutor;
    private final Processor processor;
    private final DavTazamonAdapter<Optional<User>> userDavTazamonAdapter;
    private final ServerProperties serverProperties;

    @Inject
    public DefaultUserRepository(
            @Named("propFindDavTazamonExecutor") DavTazamonExecutor davTazamonExecutor,
            Processor processor,
            DavTazamonAdapter<Optional<User>> userDavTazamonAdapter,
            ServerProperties serverProperties
    ) {
        this.davTazamonExecutor = davTazamonExecutor;
        this.processor = processor;
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
        return processor.to(propFind)
                .map(document ->
                        new DefaultDavTazamonRequest(login, password, document, serverProperties.getCalendarServer())
                )
                .flatMap(davTazamonExecutor::execute)
                .flatMap(userDavTazamonAdapter::adapt);
    }
}
