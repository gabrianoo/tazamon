package com.gability.tazamon.configuration;

import com.gability.tazamon.user.User;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        OAuth2Authentication oAuth2Authentication = getSafeObject(
                SecurityContextHolder.getContext().getAuthentication(),
                OAuth2Authentication.class
        );
        return getSafeObject(
                oAuth2Authentication.getUserAuthentication().getDetails(),
                User.class
        );
    }

    private <T> T getSafeObject(Object object, Class<T> tClass) {
        if (!tClass.isInstance(object)) {
            throw new ClassCastException();
        }
        return tClass.cast(object);
    }
}
