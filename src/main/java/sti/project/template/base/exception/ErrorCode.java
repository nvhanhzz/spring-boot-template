package sti.project.template.base.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Enum defining all error codes used throughout the application.
 * Uses i18n message keys for localization support.
 */
@Getter
public enum ErrorCode {
    // General errors (1000 - 1999)
    UNCATEGORIZED_EXCEPTION(1000, "error.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1001, "error.unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1002, "error.forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND(1003, "error.not_found", HttpStatus.NOT_FOUND),
    BAD_REQUEST(1004, "error.bad_request", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1005, "error.internal_server_error", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_EXISTS(1006, "error.already_exists", HttpStatus.BAD_REQUEST),

    // Auth errors (2000 - 2999)
    INVALID_LOGIN_CREDENTIALS(2001, "error.invalid_credentials", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2002, "error.user_not_found", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS(2003, "error.email_exists", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS(2004, "error.username_exists", HttpStatus.BAD_REQUEST),
    USER_NOT_ACTIVATED(2005, "error.user_inactive", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(2006, "error.token_expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(2007, "error.invalid_token", HttpStatus.UNAUTHORIZED),

    // Validation errors (3000 - 3999)
    VALIDATION_FAILED(3001, "error.validation_failed", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED(3002, "error.field_required", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(3003, "error.invalid_param", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(3004, "error.invalid_email", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_FORMAT(3005, "error.invalid_password", HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATION(3006, "error.constraint_violation", HttpStatus.BAD_REQUEST),
    INVALID_DATE(3007, "error.invalid_date", HttpStatus.BAD_REQUEST),
    INVALID_JSON_FORMAT(3008, "error.invalid_json_format", HttpStatus.BAD_REQUEST),

    // File/Media errors (4000 - 4999)
    MEDIA_UPLOAD_FAILED(4001, "error.file_upload_failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE(4002, "error.invalid_file", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(4003, "error.file_too_large", HttpStatus.PAYLOAD_TOO_LARGE),
    UNSUPPORTED_MEDIA_FORMAT(4005, "error.unsupported_file_type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    MEDIA_NOT_FOUND(4006, "error.media_not_found", HttpStatus.NOT_FOUND),

    // Business logic errors (5000 - 5999)
    CONFLICT(5001, "error.conflict", HttpStatus.CONFLICT),
    NAME_EXISTS(5002, "error.name_exists", HttpStatus.BAD_REQUEST),

    // Database errors (6000 - 6999)
    DUPLICATE_KEY(6001, "error.duplicate_key", HttpStatus.CONFLICT),
    DATA_INTEGRITY_VIOLATION(6002, "error.data_integrity", HttpStatus.BAD_REQUEST),
    OPTIMISTIC_LOCK_FAILURE(6003, "error.optimistic_lock", HttpStatus.CONFLICT),
    DATABASE_ERROR(6004, "error.database", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String messageKey; // i18n message key
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String messageKey, HttpStatusCode statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }
}