package com.okfunc.netkit;

import okhttp3.Cookie;

public class SimpleCookies {
    public String name;
    public String value;
    public long expiresAt;
    public String domain;
    public String path;
    public boolean secure;
    public boolean httpOnly;

    public SimpleCookies() {

    }

    public SimpleCookies(String name, String value, long expiresAt, String domain, String path, boolean secure, boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.expiresAt = expiresAt;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    public static SimpleCookies fromOkCookies(Cookie cookie) {
        return new SimpleCookies(
                cookie.name(),
                cookie.value(),
                cookie.expiresAt(),
                cookie.domain(),
                cookie.path(),
                cookie.secure(),
                cookie.httpOnly()
        );
    }

    public Cookie toOkCookie() {
        Cookie.Builder builder = new Cookie.Builder().name(name).value(value).expiresAt(expiresAt).domain(domain).path(path);
        if (secure) builder.secure();
        if (httpOnly) builder.httpOnly();
        return builder.build();
    }
}
