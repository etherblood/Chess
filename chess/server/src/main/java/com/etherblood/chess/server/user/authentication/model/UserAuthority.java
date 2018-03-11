package com.etherblood.chess.server.user.authentication.model;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Philipp
 */
public enum UserAuthority implements GrantedAuthority{

    PLAYER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

}
