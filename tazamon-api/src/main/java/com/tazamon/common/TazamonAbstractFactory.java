package com.tazamon.common;

import java.util.Optional;

public interface TazamonAbstractFactory {

    Optional<User> provideUser(String email, String password);
}
