package com.etherblood.chess.server.users.authentication.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Philipp
 */
public class UserLoginDetails {//implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private final String loginHandle;
    private final String password;
    private final Set<UserAuthority> authorities;

    public UserLoginDetails(UUID userId, String loginHandle, String password, Collection<UserAuthority> authorities) {
        this.userId = userId;
        this.loginHandle = loginHandle;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(new HashSet<>(authorities));
    }

    public UUID getUserId() {
        return userId;
    }

//    @Override
    public Set<UserAuthority> getAuthorities() {
        return authorities;
    }

//    @Override
    public String getPassword() {
        return password;
    }

//    @Override
    public String getUsername() {
        return loginHandle;
    }

//    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

//    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

//    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @Override
    public boolean isEnabled() {
        return true;
    }

}
