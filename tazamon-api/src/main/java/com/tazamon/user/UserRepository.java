package com.tazamon.user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findUser(String login, String password);
}
