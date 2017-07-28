package com.gability.tazamon.client.dav.user;

import com.gability.tazamon.client.dav.DavTazamonResponse;
import com.gability.tazamon.client.dav.xml.*;
import com.gability.tazamon.exception.ParsingException;
import com.gability.tazamon.user.User;
import com.gability.tazamon.client.dav.DavTazamonAdapter;
import com.gability.tazamon.client.dav.DavTazamonRequest;
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
                .flatMap(response ->
                        parseResponse(
                                response,
                                extractBase64EncodedToken(davTazamonResponse)
                        )
                );
    }

    private String extractBase64EncodedToken(DavTazamonResponse davTazamonResponse) {
        return Optional.ofNullable(davTazamonResponse.getDavTazamonRequest())
                .map(DavTazamonRequest::getBase64EncodeAuthToken)
                .orElseThrow(
                        () -> new ParsingException("Base64Encoded Token can't be null or missing")
                );
    }

    private Optional<User> parseResponse(Response response, String base64Token) {
        String selfLink = response.getHref();
        return Optional.of(response)
                .map(Response::getPropstat)
                .map(PropertyStatus::getProperty)
                .map(Property::getPropertyType)
                .map(propertyType -> parsePropertyType(propertyType, base64Token, selfLink))
                .orElse(Optional.empty());
    }

    private Optional<User> parsePropertyType(PropertyType propertyType, String base64Token, String selfLink) {
        if (!(propertyType instanceof CurrentUserPrincipal)) {
            throw new ParsingException("DAV PropertyType must be instance of CurrentUserPrincipal");
        }
        CurrentUserPrincipal currentUserPrincipal = ((CurrentUserPrincipal) propertyType);
        return Optional.of(
                User.builder()
                        .principal(extractPrincipal(currentUserPrincipal))
                        .selfLink(selfLink)
                        .base64EncodeAuthToken(base64Token)
                        .build()
        );
    }

    private String extractPrincipal(CurrentUserPrincipal currentUserPrincipal) {
        return Optional.ofNullable(currentUserPrincipal.getHref())
                .map(this::parsePrincipal)
                .orElseThrow(
                        () -> new ParsingException("Principal can't be null or missing")
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
