package com.juvare.ipms.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuth extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public UserAuth(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
