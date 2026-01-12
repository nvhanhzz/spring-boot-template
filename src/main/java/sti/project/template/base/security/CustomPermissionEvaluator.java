package sti.project.template.base.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Custom permission evaluator for JWT-based authorization.
 * Supports scope claim (space-separated), roles structure, and direct
 * permissions.
 * Uses Caffeine cache for high performance.
 */
@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final Cache<String, Set<String>> permissionCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .recordStats()
            .build();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (!isValidAuthentication(authentication) || !isValidPermission(permission)) {
            log.debug("Invalid authentication or permission: auth={}, permission={}",
                    authentication != null ? authentication.getClass().getSimpleName() : "null", permission);
            return false;
        }

        try {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            String requestedPermission = permission.toString().trim();

            Set<String> userPermissions = extractPermissions(jwtToken.getToken());
            boolean hasPermission = userPermissions.contains(requestedPermission);

            log.debug("Permission check: user={}, permission={}, result={}",
                    getUserIdentifier(jwtToken.getToken()), requestedPermission, hasPermission);

            return hasPermission;

        } catch (Exception e) {
            log.error("Error during permission evaluation for permission: {}", permission, e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        log.debug("Checking permission with targetId={}, targetType={}, permission={}", targetId, targetType,
                permission);
        return hasPermission(authentication, null, permission);
    }

    private boolean isValidAuthentication(Authentication authentication) {
        return authentication instanceof JwtAuthenticationToken && authentication.isAuthenticated();
    }

    private boolean isValidPermission(Object permission) {
        return permission != null && StringUtils.hasText(permission.toString());
    }

    private Set<String> extractPermissions(Jwt jwt) {
        String cacheKey = generateCacheKey(jwt);

        return permissionCache.get(cacheKey, key -> {
            log.debug("Cache miss - parsing JWT permissions for user: {}", getUserIdentifier(jwt));
            return parsePermissionsFromJwt(jwt);
        });
    }

    @SuppressWarnings("unchecked")
    private Set<String> parsePermissionsFromJwt(Jwt jwt) {
        Set<String> permissions = new HashSet<>();

        // Primary: Try authorities claim (can be String or List)
        Object authoritiesClaim = jwt.getClaim("authorities");
        if (authoritiesClaim instanceof String authorities) {
            if (StringUtils.hasText(authorities)) {
                Arrays.stream(authorities.split("\\s+"))
                        .filter(StringUtils::hasText)
                        .map(String::trim)
                        .filter(p -> !p.isEmpty())
                        .filter(p -> !p.startsWith("ROLE_"))
                        .forEach(permissions::add);
            }
        } else if (authoritiesClaim instanceof Collection<?> authoritiesList) {
            authoritiesList.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .filter(p -> !p.startsWith("ROLE_"))
                    .forEach(permissions::add);
        }

        // Secondary: Try nested roles structure
        List<Map<String, Object>> roles = jwt.getClaim("roles");
        if (!CollectionUtils.isEmpty(roles)) {
            roles.stream()
                    .filter(Objects::nonNull)
                    .map(role -> (List<String>) role.get("permissions"))
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .filter(p -> !p.startsWith("ROLE_"))
                    .forEach(permissions::add);
        }

        // Additional: Try direct permissions claim
        List<String> directPermissions = jwt.getClaim("permissions");
        if (!CollectionUtils.isEmpty(directPermissions)) {
            directPermissions.stream()
                    .filter(Objects::nonNull)
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .filter(p -> !p.startsWith("ROLE_"))
                    .forEach(permissions::add);
        }

        return permissions;
    }

    private String generateCacheKey(Jwt jwt) {
        String jti = jwt.getId();
        if (StringUtils.hasText(jti)) {
            return "jti_" + jti;
        }
        String subject = jwt.getSubject();
        Object issuedAt = jwt.getIssuedAt();
        return "sub_" + (subject != null ? subject : "unknown") + "_" +
                (issuedAt != null ? issuedAt.toString() : "no-time");
    }

    private String getUserIdentifier(Jwt jwt) {
        String subject = jwt.getSubject();
        return StringUtils.hasText(subject) ? subject : "unknown";
    }

    public void clearCache() {
        permissionCache.invalidateAll();
        log.info("Permission cache cleared");
    }

    public long getCacheSize() {
        return permissionCache.estimatedSize();
    }

    public boolean hasAnyPermission(Authentication authentication, String... permissions) {
        if (!isValidAuthentication(authentication) || permissions == null || permissions.length == 0) {
            return false;
        }

        try {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Set<String> userPermissions = extractPermissions(jwtToken.getToken());

            return Arrays.stream(permissions)
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .anyMatch(userPermissions::contains);
        } catch (Exception e) {
            log.error("Error during hasAnyPermission evaluation", e);
            return false;
        }
    }

    public boolean hasAllPermissions(Authentication authentication, String... permissions) {
        if (!isValidAuthentication(authentication) || permissions == null || permissions.length == 0) {
            return false;
        }

        try {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Set<String> userPermissions = extractPermissions(jwtToken.getToken());

            return Arrays.stream(permissions)
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .allMatch(userPermissions::contains);
        } catch (Exception e) {
            log.error("Error during hasAllPermissions evaluation", e);
            return false;
        }
    }
}
