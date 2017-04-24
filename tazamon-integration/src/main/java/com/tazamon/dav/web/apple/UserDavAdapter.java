package com.tazamon.dav.web.apple;

import com.tazamon.dav.common.XmlProcessor;
import com.tazamon.dav.web.DavAdapter;
import com.tazamon.dav.web.DavResponse;
import com.tazamon.dav.web.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class UserDavAdapter implements DavAdapter<com.tazamon.dav.web.User> {

    private static final String USER_PRINCIPAL_DAV_PROP_NAME = "current-user-principal";
    private static final Pattern PRINCIPAL_PTRN = Pattern.compile("([0-9]+)");
    private final XmlProcessor xmlProcessor;

    @Inject
    public UserDavAdapter(XmlProcessor xmlProcessor) {
        this.xmlProcessor = xmlProcessor;
    }

    @Override
    public Optional<User> adapt(DavResponse davResponse) {
        return davResponse.lookUpDavProperty(USER_PRINCIPAL_DAV_PROP_NAME)
                .flatMap(element -> xmlProcessor.fromXml(element, User.class))
                .flatMap(user -> Optional.of(
                        User.builder()
                                .href(user.getHref())
                                .principal(parsePrincipal(user.getHref()))
                                .base64EncodeAuthToken(davResponse.getDavRequest().getBase64EncodeAuthToken())
                                .build()
                        )
                );
    }

    private String parsePrincipal(String href) {
        String principal = null;
        if (href != null && !href.isEmpty()) {
            Matcher principalMatcher = PRINCIPAL_PTRN.matcher(href);
            if (principalMatcher.find()) {
                principal = principalMatcher.group();
            }
        }
        return principal;
    }
}
