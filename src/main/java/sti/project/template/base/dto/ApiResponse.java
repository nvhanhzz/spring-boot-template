package sti.project.template.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sti.project.scada.base.exception.ErrorCode;

import java.time.Instant;
import java.util.List;

/**
 * Unified API response wrapper for both success and error responses.
 *
 * @param <T> The type of data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Builder.Default
    int status = 200;

    String message;

    T data;

    String path;

    Instant timestamp;

    List<FieldErrorDetail> fieldErrors;

    // ============== SUCCESS RESPONSES ==============

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .status(201)
                .message("Created successfully")
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .status(201)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> noContent() {
        return ApiResponse.<T>builder()
                .status(204)
                .message("No content")
                .timestamp(Instant.now())
                .build();
    }

    // ============== ERROR RESPONSES ==============

    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message, String path) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message, String path,
            List<FieldErrorDetail> fieldErrors) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> fromErrorCode(ErrorCode errorCode, String path) {
        return ApiResponse.<T>builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessageKey())
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> fromErrorCode(ErrorCode errorCode, String message, String path) {
        return ApiResponse.<T>builder()
                .status(errorCode.getCode())
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    // ============== SHORTHAND ERROR METHODS ==============

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }

    public static <T> ApiResponse<T> badRequest(String message, String path) {
        return error(400, message, path);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message, String path) {
        return error(401, message, path);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }

    public static <T> ApiResponse<T> forbidden(String message, String path) {
        return error(403, message, path);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    public static <T> ApiResponse<T> notFound(String message, String path) {
        return error(404, message, path);
    }

    public static <T> ApiResponse<T> serverError(String message) {
        return error(500, message);
    }

    public static <T> ApiResponse<T> serverError(String message, String path) {
        return error(500, message, path);
    }
}