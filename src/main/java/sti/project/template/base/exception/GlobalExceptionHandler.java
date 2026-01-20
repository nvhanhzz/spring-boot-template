package sti.project.template.base.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sti.project.template.base.dto.ApiResponse;
import sti.project.template.base.dto.ApiResponseFactory;
import sti.project.template.base.dto.FieldErrorDetail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 * Provides consistent, localized error responses across all endpoints.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

        private final ApiResponseFactory responseFactory;
        private final MessageSource messageSource;

        // ============== SECURITY EXCEPTIONS ==============

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(
                        AuthorizationDeniedException ex, WebRequest request) {
                log.warn("Authorization denied: {} at path: {}", ex.getMessage(), extractPath(request));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(responseFactory.error(ErrorCode.FORBIDDEN, extractPath(request)));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
                        AccessDeniedException ex, WebRequest request) {
                log.warn("Access denied: {} at path: {}", ex.getMessage(), extractPath(request));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(responseFactory.error(ErrorCode.FORBIDDEN, extractPath(request)));
        }

        // ============== APPLICATION EXCEPTIONS ==============

        @ExceptionHandler(AppException.class)
        public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex, WebRequest request) {
                ErrorCode errorCode = ex.getErrorCode();
                log.warn("AppException: {} - Code: {}", errorCode.getMessageKey(), errorCode.getCode());

                Object[] args = ex.hasMessageArgs()
                                ? ex.getMessageArgs()
                                : new Object[] { "Resource" };

                return ResponseEntity.status(errorCode.getStatusCode())
                                .body(responseFactory.error(errorCode, extractPath(request), args));
        }

        // ============== JPA/HIBERNATE EXCEPTIONS ==============

        /**
         * Handle unique constraint violations, foreign key violations, etc.
         * Example: Duplicate email, invalid foreign key reference
         */
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
                        DataIntegrityViolationException ex, WebRequest request) {
                String message = ex.getMostSpecificCause().getMessage();
                log.warn("Data integrity violation: {}", message);

                // Check if it's a duplicate key error
                if (message != null && (message.contains("Duplicate") || message.contains("unique constraint")
                                || message.contains("UNIQUE"))) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                        .body(responseFactory.error(ErrorCode.DUPLICATE_KEY, extractPath(request)));
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.DATA_INTEGRITY_VIOLATION, extractPath(request)));
        }

        /**
         * Handle optimistic locking failures (concurrent modification).
         * Spring wrapper for JPA OptimisticLockException
         */
        @ExceptionHandler({ OptimisticLockingFailureException.class, ObjectOptimisticLockingFailureException.class })
        public ResponseEntity<ApiResponse<Void>> handleOptimisticLockingFailureException(
                        Exception ex, WebRequest request) {
                log.warn("Optimistic lock failure: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(responseFactory.error(ErrorCode.OPTIMISTIC_LOCK_FAILURE, extractPath(request)));
        }

        /**
         * Handle JPA OptimisticLockException directly
         */
        @ExceptionHandler(OptimisticLockException.class)
        public ResponseEntity<ApiResponse<Void>> handleOptimisticLockException(
                        OptimisticLockException ex, WebRequest request) {
                log.warn("JPA Optimistic lock: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(responseFactory.error(ErrorCode.OPTIMISTIC_LOCK_FAILURE, extractPath(request)));
        }

        /**
         * Handle JPA EntityNotFoundException
         */
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(
                        EntityNotFoundException ex, WebRequest request) {
                log.warn("Entity not found: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(responseFactory.error(ErrorCode.NOT_FOUND, extractPath(request)));
        }

        /**
         * Handle JPA PersistenceException (general JPA errors)
         */
        @ExceptionHandler(PersistenceException.class)
        public ResponseEntity<ApiResponse<Void>> handlePersistenceException(
                        PersistenceException ex, WebRequest request) {
                log.error("JPA Persistence error: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(responseFactory.error(ErrorCode.DATABASE_ERROR, extractPath(request)));
        }

        /**
         * Handle Spring DataAccessException (general database errors)
         */
        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ApiResponse<Void>> handleDataAccessException(
                        DataAccessException ex, WebRequest request) {
                log.error("Data access error: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(responseFactory.error(ErrorCode.DATABASE_ERROR, extractPath(request)));
        }

        // ============== VALIDATION EXCEPTIONS ==============

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex, WebRequest request) {
                log.warn("Validation failed: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.VALIDATION_FAILED, extractPath(request),
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

                log.warn("Validation failed: {}", fieldErrors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.VALIDATION_FAILED, extractPath(request),
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
                                .body(responseFactory.error(ErrorCode.VALIDATION_FAILED, extractPath(request),
                                                fieldErrors));
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ApiResponse<Void>> handleMissingParameterException(
                        MissingServletRequestParameterException ex, WebRequest request) {
                log.warn("Missing parameter: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.INVALID_PARAM, extractPath(request)));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException ex, WebRequest request) {
                log.warn("Type mismatch for parameter: {}", ex.getName());

                Class<?> requiredType = ex.getRequiredType();
                if (requiredType != null && requiredType.isEnum()) {
                        Object[] enumConstants = requiredType.getEnumConstants();
                        String validValues = Arrays.stream(enumConstants)
                                        .map(Object::toString)
                                        .collect(Collectors.joining(", "));
                        String paramName = ex.getName();
                        String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";

                        Locale locale = LocaleContextHolder.getLocale();
                        String localizedMessage = messageSource.getMessage(
                                        "error.invalid_enum_value.detail",
                                        new Object[] { invalidValue, paramName, validValues },
                                        locale);

                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(responseFactory.error(
                                                        ErrorCode.INVALID_ENUM_VALUE.getCode(),
                                                        localizedMessage,
                                                        extractPath(request),
                                                        null));
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.INVALID_PARAM, extractPath(request)));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex, WebRequest request) {
                Throwable cause = ex.getMostSpecificCause();
                String message = cause.getMessage();
                log.warn("Malformed JSON: {}", message);

                if (cause instanceof InvalidFormatException invalidFormatEx) {
                        Class<?> targetType = invalidFormatEx.getTargetType();
                        if (targetType != null && targetType.isEnum()) {
                                Object[] enumConstants = targetType.getEnumConstants();
                                String validValues = Arrays.stream(enumConstants)
                                                .map(Object::toString)
                                                .collect(Collectors.joining(", "));
                                String fieldName = invalidFormatEx.getPath().isEmpty() ? "field"
                                                : invalidFormatEx.getPath().get(0).getFieldName();
                                String invalidValue = String.valueOf(invalidFormatEx.getValue());

                                Locale locale = LocaleContextHolder.getLocale();
                                String localizedMessage = messageSource.getMessage(
                                                "error.invalid_enum_value.detail",
                                                new Object[] { invalidValue, fieldName, validValues },
                                                locale);

                                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                                .body(responseFactory.error(
                                                                ErrorCode.INVALID_ENUM_VALUE.getCode(),
                                                                localizedMessage,
                                                                extractPath(request),
                                                                null));
                        }
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.INVALID_JSON_FORMAT, extractPath(request)));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
                        IllegalArgumentException ex, WebRequest request) {
                log.warn("Illegal argument: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(responseFactory.error(ErrorCode.BAD_REQUEST, extractPath(request)));
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
                                .body(responseFactory.error(ErrorCode.INTERNAL_SERVER_ERROR, extractPath(request)));
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
