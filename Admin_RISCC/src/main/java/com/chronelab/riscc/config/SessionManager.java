package com.chronelab.riscc.config;

import com.chronelab.riscc.enums.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SessionManager {
    public static AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getUserId() {
        return getAuthenticatedUser().getId();
    }

    public static boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

    public static List<Authority> getAuthorities() {
        List<Authority> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : getAuthenticatedUser().getAuthorities()) {
            authorities.add(Authority.valueOf(grantedAuthority.getAuthority()));
        }
        return authorities;
    }

    public static String getRole() {
        return getAuthenticatedUser().getRole();
    }

    public static boolean isSuperAdmin() {
        return !isAnonymousUser() && getAuthenticatedUser().getRole().equalsIgnoreCase("Super Admin");
    }

    public static List<Authority> getAllAuthorities() {
        List<Authority> authorities = new ArrayList<>();
        Collections.addAll(authorities, Authority.values());
        return authorities;
    }
}
