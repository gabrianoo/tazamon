package com.tazamon.dav.web.apple;

import com.tazamon.dav.common.XmlProcessor;
import com.tazamon.dav.web.DavResponse;
import com.tazamon.dav.web.DavResponseAdapter;
import com.tazamon.dav.web.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class UserDavResponseAdapter implements DavResponseAdapter<User> {

    private static final String USER_PRINCIPAL_DAV_PROP_NAME = "current-user-principal";
    private static final Pattern PRINCIPAL_PTRN = Pattern.compile("([0-9]+)");
    private final XmlProcessor xmlProcessor;

    @Inject
    public UserDavResponseAdapter(XmlProcessor xmlProcessor) {
        this.xmlProcessor = xmlProcessor;
    }

    @Override
    public Optional<User> adapt(DavResponse davResponse) {
        return davResponse.lookUpDavProperty(USER_PRINCIPAL_DAV_PROP_NAME)
                .flatMap(element -> xmlProcessor.fromXml(element, CurrentUserPrincipal.class))
                .flatMap(currentUserPrincipal -> parsePrincipal(currentUserPrincipal.getHref()))
                .flatMap(principal -> Optional.of(User.builder().principal(principal).build()));
    }

    private Optional<String> parsePrincipal(String href) {
        Optional<String> principalOptional = Optional.empty();
        if (href != null && !href.isEmpty()) {
            Matcher principalMatcher = PRINCIPAL_PTRN.matcher(href);
            if (principalMatcher.find()) {
                principalOptional = Optional.of(principalMatcher.group());
            }
        }
        return principalOptional;
    }
}
