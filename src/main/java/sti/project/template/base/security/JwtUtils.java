package sti.project.template.base.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
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

@Slf4j
@Component
public class JwtUtils {

    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    public static final String TOKEN_TYPE_CLAIM = "token_type";

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.access-token-expiration:3600}")
    private long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration:604800}")
    private long refreshTokenExpiration;

    @Value("${app.security.jwt.authorities-claim-name:authorities}")
    private String authoritiesClaimName;

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, TOKEN_TYPE_ACCESS, true);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, TOKEN_TYPE_REFRESH, false);
    }

    private String generateToken(User user, long expirationSeconds, String tokenType, boolean includeAuthorities) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Instant now = Instant.now();
        Instant expiration = now.plus(expirationSeconds, ChronoUnit.SECONDS);

        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer("template-api")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString())
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .claim("email", user.getEmail())
                .claim("name", user.getName());

        if (includeAuthorities) {
            Set<String> authorities = buildAuthorities(user);
            claimsBuilder.claim(authoritiesClaimName, authorities);
        }

        JWTClaimsSet claimsSet = claimsBuilder.build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            log.error("Cannot create token", e);
            throw new RuntimeException("Cannot create token", e);
        }
    }

    private Set<String> buildAuthorities(User user) {
        Set<String> authorities = new HashSet<>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                authorities.add("ROLE_" + role.getName());

                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        authorities.add(permission.getName());
                    }
                }
            }
        }

        return authorities;
    }

    public UUID getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return UUID.fromString(signedJWT.getJWTClaimsSet().getSubject());
        } catch (Exception e) {
            log.error("Cannot get user ID from token", e);
            return null;
        }
    }

    public String getTokenType(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return (String) signedJWT.getJWTClaimsSet().getClaim(TOKEN_TYPE_CLAIM);
        } catch (Exception e) {
            log.error("Cannot get token type from token", e);
            return null;
        }
    }

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
