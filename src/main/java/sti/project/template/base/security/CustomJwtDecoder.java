package sti.project.template.base.security;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Custom JWT decoder using HMAC secret key for verification.
 */
public class CustomJwtDecoder implements JwtDecoder {

    private final JWSVerifier verifier;

    public CustomJwtDecoder(String secretKey) throws JwtException {
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalArgumentException("JWT secret key must not be null or empty.");
        }

        try {
            this.verifier = new MACVerifier(secretKey.getBytes());
        } catch (Exception e) {
            throw new JwtException("Failed to create JWS verifier.", e);
        }
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Verify signature
            if (!signedJWT.verify(this.verifier)) {
                throw new BadJwtException("Invalid JWT signature.");
            }

            // Check expiration
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new BadJwtException("JWT has expired.");
            }

            // Extract claims if valid
            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims());

        } catch (ParseException e) {
            throw new BadJwtException("Invalid JWT token format.", e);
        } catch (Exception e) {
            throw new JwtException("An error occurred during JWT decoding.", e);
        }
    }
}
