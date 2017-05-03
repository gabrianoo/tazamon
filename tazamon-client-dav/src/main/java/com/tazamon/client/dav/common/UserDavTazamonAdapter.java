package com.tazamon.client.dav.common;

import com.tazamon.client.dav.DavTazamonAdapter;
import com.tazamon.client.dav.DavTazamonRequest;
import com.tazamon.client.dav.DavTazamonResponse;
import com.tazamon.client.dav.xml.*;
import com.tazamon.common.User;
import com.tazamon.exception.UserParseException;
import lombok.NonNull;

import javax.inject.Named;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public final class UserDavTazamonAdapter implements DavTazamonAdapter<Optional<User>> {

    private static final Pattern PRINCIPAL_PTRN = Pattern.compile("([0-9]+)");

    @Override
    public Optional<User> adapt(@NonNull DavTazamonResponse davTazamonResponse) {
        return Optional.ofNullable(davTazamonResponse.getMultiStatus())
                .map(MultiStatus::getResponse)
                .flatMap(responses -> responses.stream().findFirst())
                .map(Response::getPropstat)
                .map(PropertyStatus::getProperty)
                .map(Property::getPropertyType)
                .map(propertyType ->
                        parsePropertyType(
                                propertyType,
                                extractBase64EncodedToken(davTazamonResponse)
                        )
                );
    }

    private String extractBase64EncodedToken(DavTazamonResponse davTazamonResponse) {
        return Optional.ofNullable(davTazamonResponse.getDavTazamonRequest())
                .map(DavTazamonRequest::getBase64EncodeAuthToken)
                .orElseThrow(
                        () -> new UserParseException("Base64Encoded Token can't be null or missing")
                );
    }

    private User parsePropertyType(PropertyType propertyType, String base64Token) {
        if (!(propertyType instanceof CurrentUserPrincipal)) {
            throw new UserParseException("DAV PropertyType must be instance of CurrentUserPrincipal");
        }
        CurrentUserPrincipal currentUserPrincipal = ((CurrentUserPrincipal) propertyType);
        return User.builder()
                .principal(extractPrincipal(currentUserPrincipal))
                .base64EncodeAuthToken(base64Token)
                .build();
    }

    private String extractPrincipal(CurrentUserPrincipal currentUserPrincipal) {
        return Optional.ofNullable(currentUserPrincipal.getHref())
                .map(this::parsePrincipal)
                .orElseThrow(
                        () -> new UserParseException("Principal can't be null or missing")
                );
    }

    private String parsePrincipal(String href) {
        Matcher principalMatcher = PRINCIPAL_PTRN.matcher(href);
        String match = null;
        if (principalMatcher.find()) {
            match = principalMatcher.group();
        }
        return match;
    }
}
