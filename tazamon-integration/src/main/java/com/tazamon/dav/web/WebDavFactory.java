package com.tazamon.dav.web;

import java.util.Optional;

public interface WebDavFactory {

    Optional<User> provideUser(String email, String password);
}
