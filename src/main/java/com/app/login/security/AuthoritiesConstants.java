package com.app.login.security;

/**
 * Constants for Spring Security authorities.
 * @author megadotnet
 * @date 2017-12-13
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
