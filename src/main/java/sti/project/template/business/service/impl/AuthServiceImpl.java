package sti.project.template.business.service.impl;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.security.AuthUserContext;
import sti.project.template.base.security.JwtUtils;
import sti.project.template.business.dto.request.LoginRequest;
import sti.project.template.business.dto.request.RefreshTokenRequest;
import sti.project.template.business.dto.request.UpdateProfileRequest;
import sti.project.template.business.dto.response.AuthResponse;
import sti.project.template.business.dto.response.UserResponse;
import sti.project.template.business.entity.User;
import sti.project.template.business.mapper.UserMapper;
import sti.project.template.business.repository.UserRepository;
import sti.project.template.business.service.AuthService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    AuthUserContext authUserContext;

    @NonFinal
    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = findUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVATED);
        }

        return generateAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (!validateToken(request.getRefreshToken())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        UUID userId = jwtUtils.getUserIdFromToken(request.getRefreshToken());
        if (userId == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        User user = userRepository.findByIdAndStatusNot(userId, EntityStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return generateAuthResponse(user);
    }

    @Override
    public void logout(String token) {
        log.info("User logged out");
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe() {
        User user = getCurrentUser();
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateMe(UpdateProfileRequest request) {
        User user = getCurrentUser();
        userMapper.updateFromProfileRequest(request, user);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    private User getCurrentUser() {
        UUID userId = authUserContext.getUserIdAsUUID();
        if (userId == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return userRepository.findByIdAndStatusNot(userId, EntityStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmailAndStatusNot(email, EntityStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_LOGIN_CREDENTIALS));
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return AuthResponse.of(accessToken, refreshToken, userResponse);
    }

    private boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(new MACVerifier(secretKey.getBytes()))) {
                return false;
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && expirationTime.after(new Date());

        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }
}
