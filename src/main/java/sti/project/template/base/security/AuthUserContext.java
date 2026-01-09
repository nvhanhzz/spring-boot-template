package sti.project.template.base.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Helper component to access authenticated user information from
 * SecurityContext.
 */
@Component
public class AuthUserContext {

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Check if user is currently authenticated.
     */
    public boolean isLoggedIn() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Get current user ID from SecurityContext (JWT subject).
     * 
     * @return User ID as String, or null if not authenticated.
     */
    public String getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * Get current user ID as UUID.
     * 
     * @return User ID as UUID, or null if not authenticated or invalid format.
     */
    public UUID getUserIdAsUUID() {
        String userId = getUserId();
        if (userId == null) {
            return null;
        }
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get user roles (authorities with ROLE_ prefix).
     * 
     * @return Set of role names without ROLE_ prefix.
     */
    public Set<String> getRoles() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptySet();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring("ROLE_".length()))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    /**
     * Get user permissions (authorities without ROLE_ prefix).
     * 
     * @return Set of permission names.
     */
    public Set<String> getPermissions() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptySet();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> !auth.startsWith("ROLE_"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    /**
     * Check if user has a specific role.
     */
    public boolean hasRole(String role) {
        return getRoles().contains(role.toLowerCase());
    }

    /**
     * Check if user has a specific permission.
     */
    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission.toLowerCase());
    }
}
