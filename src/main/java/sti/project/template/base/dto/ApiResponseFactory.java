package sti.project.template.base.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.i18n.MessageHelper;

import java.util.List;

/**
 * Factory for creating ApiResponse with automatic i18n message resolution.
 * 
 * Usage in GlobalExceptionHandler:
 * return responseFactory.error(ErrorCode.NOT_FOUND, path);
 * 
 * Usage in Controller:
 * return responseFactory.success(data, "success.created");
 */
@Component
@RequiredArgsConstructor
public class ApiResponseFactory {

    private final MessageHelper messageHelper;

    // ============== SUCCESS RESPONSES ==============

    public <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    public <T> ApiResponse<T> success(T data, String messageKey) {
        return ApiResponse.success(data, messageHelper.getMessage(messageKey));
    }

    public <T> ApiResponse<T> success(T data, String messageKey, Object... args) {
        return ApiResponse.success(data, messageHelper.getMessage(messageKey, args));
    }

    public <T> ApiResponse<T> created(T data) {
        return ApiResponse.created(data, messageHelper.getMessage("success.created"));
    }

    public <T> ApiResponse<T> created(T data, String messageKey) {
        return ApiResponse.created(data, messageHelper.getMessage(messageKey));
    }

    // ============== ERROR RESPONSES ==============

    /**
     * Create error response from ErrorCode with auto i18n message.
     */
    public <T> ApiResponse<T> error(ErrorCode errorCode, String path) {
        return ApiResponse.error(
                errorCode.getCode(),
                messageHelper.getMessage(errorCode.getMessageKey()),
                path);
    }

    /**
     * Create error response with custom message key.
     */
    public <T> ApiResponse<T> error(ErrorCode errorCode, String messageKey, String path) {
        return ApiResponse.error(
                errorCode.getCode(),
                messageHelper.getMessage(messageKey),
                path);
    }

    /**
     * Create error response with field errors (for validation).
     */
    public <T> ApiResponse<T> error(ErrorCode errorCode, String path, List<FieldErrorDetail> fieldErrors) {
        return ApiResponse.error(
                errorCode.getCode(),
                messageHelper.getMessage(errorCode.getMessageKey()),
                path,
                fieldErrors);
    }

    /**
     * Create error response with custom message and field errors.
     */
    public <T> ApiResponse<T> error(int status, String message, String path, List<FieldErrorDetail> fieldErrors) {
        return ApiResponse.error(status, message, path, fieldErrors);
    }
}
