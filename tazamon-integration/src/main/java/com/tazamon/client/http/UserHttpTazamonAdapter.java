package com.tazamon.client.http;

import com.tazamon.common.User;
import com.tazamon.common.XmlProcessor;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class UserHttpTazamonAdapter implements HttpTazamonAdapter<Optional<User>, DavPropertySet> {

    private static final String USER_PRINCIPAL_DAV_PROP_NAME = "current-user-principal";
    private static final Pattern PRINCIPAL_PTRN = Pattern.compile("([0-9]+)");
    private final XmlProcessor xmlProcessor;
    private final HttpTazamonResponsePropertyLookUp<DavPropertySet> httpTazamonResponsePropertyLookUp;

    @Inject
    public UserHttpTazamonAdapter(
            XmlProcessor xmlProcessor,
            HttpTazamonResponsePropertyLookUp<DavPropertySet> httpTazamonResponsePropertyLookUp
    ) {
        this.xmlProcessor = xmlProcessor;
        this.httpTazamonResponsePropertyLookUp = httpTazamonResponsePropertyLookUp;
    }

    @Override
    public Optional<User> adapt(HttpTazamonResponse<DavPropertySet> httpTazamonResponse) {
        return httpTazamonResponsePropertyLookUp.lookUpProperty(httpTazamonResponse, USER_PRINCIPAL_DAV_PROP_NAME)
                .flatMap(element -> xmlProcessor.fromXml(element, User.class))
                .map(user -> User.builder()
                        .href(user.getHref())
                        .principal(parsePrincipal(user.getHref()))
                        .base64EncodeAuthToken(
                                httpTazamonResponse.getHttpTazamonRequest().getBase64EncodeAuthToken()
                        )
                        .build()
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
