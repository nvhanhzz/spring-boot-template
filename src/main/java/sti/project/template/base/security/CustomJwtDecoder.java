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

            if (!signedJWT.verify(this.verifier)) {
                throw new BadJwtException("Invalid JWT signature.");
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new BadJwtException("JWT has expired.");
            }

            // Verify token type - only access tokens allowed for API access
            String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim(JwtUtils.TOKEN_TYPE_CLAIM);
            if (!JwtUtils.TOKEN_TYPE_ACCESS.equals(tokenType)) {
                throw new BadJwtException("Invalid token type. Only access tokens are allowed.");
            }

            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims());

        } catch (ParseException e) {
            throw new BadJwtException("Invalid JWT token format.", e);
        } catch (BadJwtException e) {
            throw e;
        } catch (Exception e) {
            throw new JwtException("An error occurred during JWT decoding.", e);
        }
    }
}
