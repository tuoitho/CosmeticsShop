package com.cosmeticsellingwebsite.security;

import com.cosmeticsellingwebsite.entity.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Data
public class UserPrincipal implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    Long userId;
    String userName;
    String password = null;
    String role = null;
    Set<SimpleGrantedAuthority> authorities;

    public UserPrincipal(User user) {
        userId = user.getUserId();
        userName = user.getUsername();
        password = user.getPassword();
        role = user.getRole().getRoleName().toString();
        authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_"+role));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
}
