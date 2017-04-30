package com.tazamon.client.http;

import com.tazamon.common.User;

import java.util.Optional;

public interface HttpTazamonAbstractFactory {

    Optional<User> provideUser(String email, String password);
}
