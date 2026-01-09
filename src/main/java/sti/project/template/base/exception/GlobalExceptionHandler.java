package sti.project.template.base.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sti.project.template.base.dto.ApiResponse;
import sti.project.template.base.dto.FieldErrorDetail;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 * Provides consistent error responses across all endpoints.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        // ============== SECURITY EXCEPTIONS ==============

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(
                        AuthorizationDeniedException ex, WebRequest request) {
                log.warn("Authorization denied: {} at path: {}", ex.getMessage(), extractPath(request));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.fromErrorCode(ErrorCode.FORBIDDEN, extractPath(request)));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
                        AccessDeniedException ex, WebRequest request) {
                log.warn("Access denied: {} at path: {}", ex.getMessage(), extractPath(request));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.fromErrorCode(ErrorCode.FORBIDDEN, extractPath(request)));
        }

        @ExceptionHandler(ForbiddenException.class)
        public ResponseEntity<ApiResponse<Void>> handleForbiddenException(
                        ForbiddenException ex, WebRequest request) {
                log.warn("Forbidden: {} at path: {}", ex.getMessage(), extractPath(request));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.fromErrorCode(ErrorCode.FORBIDDEN, ex.getMessage(),
                                                extractPath(request)));
        }

        // ============== APPLICATION EXCEPTIONS ==============

        @ExceptionHandler(AppException.class)
        public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex, WebRequest request) {
                ErrorCode errorCode = ex.getErrorCode();
                log.warn("AppException: {} - Code: {}", ex.getMessage(), errorCode.getCode());
                return ResponseEntity.status(errorCode.getStatusCode())
                                .body(ApiResponse.fromErrorCode(errorCode, ex.getMessage(), extractPath(request)));
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex, WebRequest request) {
                log.warn("Resource not found: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.fromErrorCode(ErrorCode.NOT_FOUND, ex.getMessage(),
                                                extractPath(request)));
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex, WebRequest request) {
                log.warn("Bad request: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.fromErrorCode(ErrorCode.BAD_REQUEST, ex.getMessage(),
                                                extractPath(request)));
        }

        // ============== VALIDATION EXCEPTIONS ==============

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex, WebRequest request) {
                log.warn("Validation failed: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(
                                                ErrorCode.VALIDATION_FAILED.getCode(),
                                                ex.getMessage(),
                                                extractPath(request),
                                                ex.getFieldErrors()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex, WebRequest request) {
                List<FieldErrorDetail> fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                                .collect(Collectors.toList());

                String firstError = ex.getBindingResult().getFieldErrors().stream()
                                .findFirst()
                                .map(FieldError::getDefaultMessage)
                                .orElse("Validation failed");

                log.warn("Validation failed: {}", fieldErrors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(
                                                ErrorCode.VALIDATION_FAILED.getCode(),
                                                firstError,
                                                extractPath(request),
                                                fieldErrors));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
                        ConstraintViolationException ex, WebRequest request) {
                List<FieldErrorDetail> fieldErrors = ex.getConstraintViolations()
                                .stream()
                                .map(v -> new FieldErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
                                .collect(Collectors.toList());

                log.warn("Constraint violation: {}", fieldErrors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(
                                                ErrorCode.VALIDATION_FAILED.getCode(),
                                                "Validation failed",
                                                extractPath(request),
                                                fieldErrors));
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ApiResponse<Void>> handleMissingParameterException(
                        MissingServletRequestParameterException ex, WebRequest request) {
                log.warn("Missing parameter: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.fromErrorCode(ErrorCode.INVALID_PARAM, ex.getMessage(),
                                                extractPath(request)));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException ex, WebRequest request) {
                String paramName = ex.getName();
                String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
                String providedValue = ex.getValue() != null ? ex.getValue().toString() : "null";

                String errorMessage = String.format(
                                "Parameter '%s' has invalid type. Expected '%s' but received '%s'",
                                paramName, requiredType, providedValue);

                log.warn("Type mismatch: {}", errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.fromErrorCode(ErrorCode.INVALID_PARAM, errorMessage,
                                                extractPath(request)));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex, WebRequest request) {
                String message = ex.getMostSpecificCause().getMessage();
                log.warn("Malformed JSON: {}", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.fromErrorCode(ErrorCode.INVALID_JSON_FORMAT, message,
                                                extractPath(request)));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
                        IllegalArgumentException ex, WebRequest request) {
                log.warn("Illegal argument: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.fromErrorCode(ErrorCode.BAD_REQUEST, ex.getMessage(),
                                                extractPath(request)));
        }

        // ============== CLIENT DISCONNECT ==============

        @ExceptionHandler(AsyncRequestNotUsableException.class)
        public void handleAsyncRequestNotUsable(AsyncRequestNotUsableException ex, WebRequest request) {
                log.debug("Client disconnected: {}", extractPath(request));
        }

        // ============== GENERIC EXCEPTION ==============

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex, WebRequest request) {
                if (isBrokenPipeException(ex)) {
                        log.debug("Client disconnected (Broken pipe): {}", extractPath(request));
                        return null;
                }

                log.error("Unexpected error: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.fromErrorCode(ErrorCode.INTERNAL_SERVER_ERROR, extractPath(request)));
        }

        // ============== HELPER METHODS ==============

        private boolean isBrokenPipeException(Exception ex) {
                if (ex instanceof IOException ioEx) {
                        String message = ioEx.getMessage();
                        return message != null && (message.contains("Broken pipe") ||
                                        message.contains("Connection reset by peer") ||
                                        message.contains("Connection timed out"));
                }

                Throwable cause = ex.getCause();
                if (cause instanceof IOException) {
                        String message = cause.getMessage();
                        return message != null && (message.contains("Broken pipe") ||
                                        message.contains("Connection reset by peer") ||
                                        message.contains("Connection timed out"));
                }

                return false;
        }

        private String extractPath(WebRequest request) {
                if (request instanceof ServletWebRequest servletRequest) {
                        return servletRequest.getRequest().getRequestURI();
                }
                return "";
        }
}
