package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sti.project.scada.base.constant.ApiVersion;
import sti.project.scada.base.dto.ApiResponse;
import sti.project.scada.base.dto.ApiResponseFactory;
import sti.project.scada.business.dto.request.LoginRequest;
import sti.project.scada.business.dto.request.RefreshTokenRequest;
import sti.project.scada.business.dto.request.UpdateProfileRequest;
import sti.project.scada.business.dto.response.AuthResponse;
import sti.project.scada.business.dto.response.UserResponse;
import sti.project.scada.business.service.AuthService;

@RestController
@RequestMapping(ApiVersion.V1 + "/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ApiResponseFactory responseFactory;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user with email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(responseFactory.success(response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Get new access token using refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(responseFactory.success(response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout and invalidate the current session")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok(responseFactory.success(null));
    }

    @GetMapping("/me")
    @Operation(summary = "Get Profile", description = "Get current authenticated user profile")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        UserResponse response = authService.getMe();
        return ResponseEntity.ok(responseFactory.success(response));
    }

    @PutMapping("/me")
    @Operation(summary = "Update Profile", description = "Update current authenticated user profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponse response = authService.updateMe(request);
        return ResponseEntity.ok(responseFactory.success(response));
    }
}
