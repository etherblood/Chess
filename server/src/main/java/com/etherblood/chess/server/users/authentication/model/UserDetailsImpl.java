package com.etherblood.chess.server.users.authentication.model;

import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Philipp
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private final String loginHandle;
    private final String password;
    private final Set<UserAuthority> authorities;

    public UserDetailsImpl(UUID userId, String loginHandle, String password, Set<UserAuthority> authorities) {
        this.userId = userId;
        this.loginHandle = loginHandle;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Set<UserAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginHandle;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getUserId() {
        return userId;
    }

}
