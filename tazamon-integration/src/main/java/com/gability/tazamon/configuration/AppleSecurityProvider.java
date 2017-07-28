package com.gability.tazamon.configuration;

import com.gability.tazamon.user.User;
import com.gability.tazamon.user.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Optional;

@Named
public class AppleSecurityProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Inject
    public AppleSecurityProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> userOptional = userRepository.findUser(username, password);
        if (!userOptional.isPresent()) {
            throw new BadCredentialsException("Username or password is invalid.");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        authenticationToken.setDetails(userOptional.get());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
