package sti.project.template.base.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sti.project.template.business.entity.Permission;
import sti.project.template.business.entity.Role;
import sti.project.template.business.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Utility class for JWT token generation and validation.
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.access-token-expiration:3600}")
    private long accessTokenExpiration; // in seconds

    @Value("${app.security.jwt.refresh-token-expiration:604800}")
    private long refreshTokenExpiration; // in seconds (default 7 days)

    @Value("${app.security.jwt.authorities-claim-name:authorities}")
    private String authoritiesClaimName;

    /**
     * Generate access token for user
     */
    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration);
    }

    /**
     * Generate refresh token for user
     */
    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration);
    }

    private String generateToken(User user, long expirationSeconds) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Instant now = Instant.now();
        Instant expiration = now.plus(expirationSeconds, ChronoUnit.SECONDS);

        // Build authorities from roles and permissions
        Set<String> authorities = buildAuthorities(user);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer("template-api")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim(authoritiesClaimName, authorities)
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException("Cannot create token", e);
        }
    }

    private Set<String> buildAuthorities(User user) {
        Set<String> authorities = new HashSet<>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                // Add role with ROLE_ prefix
                authorities.add("ROLE_" + role.getName());

                // Add permissions
                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        authorities.add(permission.getName());
                    }
                }
            }
        }

        return authorities;
    }

    /**
     * Get user ID from token
     */
    public UUID getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return UUID.fromString(signedJWT.getJWTClaimsSet().getSubject());
        } catch (Exception e) {
            log.error("Cannot get user ID from token", e);
            return null;
        }
    }

    /**
     * Get authorities from token
     */
    @SuppressWarnings("unchecked")
    public Set<String> getAuthoritiesFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Object authoritiesClaim = signedJWT.getJWTClaimsSet().getClaim(authoritiesClaimName);
            if (authoritiesClaim instanceof Collection) {
                return new HashSet<>((Collection<String>) authoritiesClaim);
            }
            return new HashSet<>();
        } catch (Exception e) {
            log.error("Cannot get authorities from token", e);
            return new HashSet<>();
        }
    }
}
