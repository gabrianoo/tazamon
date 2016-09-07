package com.otasys.tazamon.web.dav;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserPrincipalTest {

    private static final String INVALID_PRINCIPAL = "/invalid/principal";
    private static final String VALID_PRINCIPAL = "/123456/principal";

    @Test
    public void givenUserPrincipalWhenSettingHrefWithNullThenUserPrincipalShouldBeNull() {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setHref(null);
        assertThat(userPrincipal.getHref(), is(equalTo(null)));
        assertThat(userPrincipal.getPrincipal(), is(equalTo(null)));
    }

    @Test
    public void givenUserPrincipalWhenSettingEmptyHrefThenUserPrincipalShouldBeNullAndHrefIsEmpty() {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setHref("");
        assertThat(userPrincipal.getHref(), is(equalTo("")));
        assertThat(userPrincipal.getPrincipal(), is(equalTo(null)));
    }

    @Test
    public void givenUserPrincipalWhenSettingInvalidHrefThenUserPrincipalShouldBeNull() {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setHref(INVALID_PRINCIPAL);
        assertThat(userPrincipal.getHref(), is(equalTo(INVALID_PRINCIPAL)));
        assertThat(userPrincipal.getPrincipal(), is(equalTo(null)));
    }

    @Test
    public void givenUserPrincipalWhenSettingValidHrefThenUserPrincipalShouldBeValid() {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setHref(VALID_PRINCIPAL);
        assertThat(userPrincipal.getHref(), is(equalTo(VALID_PRINCIPAL)));
        assertThat(userPrincipal.getPrincipal(), is(equalTo("123456")));
    }
}
