package com.tazamon.client.dav;

import com.tazamon.common.User;

import javax.inject.Named;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class UserDavTazamonAdapter implements DavTazamonAdapter<Optional<User>> {

    private static final Pattern PRINCIPAL_PTRN = Pattern.compile("([0-9]+)");

    @Override
    public Optional<User> adapt(DavTazamonResponse davTazamonResponse) {
        return davTazamonResponse.getMultiStatus().getResponse()
                .stream().findFirst().flatMap(
                        response -> {
                            Optional<User> userOptional = Optional.empty();
                            PropertyType propertyType = response.getPropstat().getProperty().getPropertyType();
                            if (propertyType instanceof CurrentUserPrincipal) {
                                CurrentUserPrincipal currentUserPrincipal = ((CurrentUserPrincipal) propertyType);
                                userOptional = Optional.of(
                                        User.builder()
                                                .principal(parsePrincipal(currentUserPrincipal.getHref()))
                                                .base64EncodeAuthToken(
                                                        davTazamonResponse
                                                                .getDavTazamonRequest().getBase64EncodeAuthToken()
                                                )
                                                .build()
                                );
                            }
                            return userOptional;
                        }
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
