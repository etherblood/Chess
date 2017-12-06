package com.etherblood.chess.server.users.authentication.model;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Philipp
 */
public enum UserAuthority implements GrantedAuthority{

    GUEST, PLAYER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

}
