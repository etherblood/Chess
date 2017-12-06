package com.etherblood.chess.server.users.authentication.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

/**
 *
 * @author Philipp
 */
public class GuestAuthentication implements Authentication {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final String name;

    public GuestAuthentication(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public List<UserAuthority> getAuthorities() {
        return Arrays.asList(UserAuthority.GUEST);
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getPrincipal() {
        return name;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return name;
    }

}
