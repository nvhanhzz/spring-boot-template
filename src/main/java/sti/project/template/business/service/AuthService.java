package sti.project.template.business.service;

import sti.project.template.business.dto.request.LoginRequest;
import sti.project.template.business.dto.request.RefreshTokenRequest;
import sti.project.template.business.dto.request.UpdateProfileRequest;
import sti.project.template.business.dto.response.AuthResponse;
import sti.project.template.business.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String token);

    UserResponse getMe();

    UserResponse updateMe(UpdateProfileRequest request);
}