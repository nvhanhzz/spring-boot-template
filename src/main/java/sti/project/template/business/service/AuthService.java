package sti.project.template.business.service;

import sti.project.template.business.dto.request.LoginRequest;
import sti.project.template.business.dto.request.RefreshTokenRequest;
import sti.project.template.business.dto.request.UpdateProfileRequest;
import sti.project.template.business.dto.response.AuthResponse;
import sti.project.template.business.dto.response.UserResponse;

/**
 * Authentication service interface.
 */
public interface AuthService {

    /**
     * Login with email and password
     */
    AuthResponse login(LoginRequest request);

    /**
     * Refresh access token using refresh token
     */
    AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * Logout (invalidate token)
     */
    void logout(String token);

    /**
     * Get current user profile
     */
    UserResponse getMe();

    /**
     * Update current user profile
     */
    UserResponse updateMe(UpdateProfileRequest request);
}
