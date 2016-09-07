package com.otasys.tazamon.web.dav;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement
public class UserPrincipal {

    private String href;
    private String principal;
    private static final Pattern PRINCIPAL_PTRN = Pattern.compile("([0-9]+)");

    public String getHref() {
        return href;
    }

    public String getPrincipal() {
        return principal;
    }

    @XmlElement
    public void setHref(String href) {
        this.href = href;
        setPrincipal(href);
    }

    private void setPrincipal(String href) {
        if (href == null || href.isEmpty()) {
            this.principal = null;
            return;
        }
        Matcher principalMatcher = PRINCIPAL_PTRN.matcher(href);
        if (principalMatcher.find()) {
            this.principal = principalMatcher.group();
        } else {
            this.principal = null;
        }
    }
}
