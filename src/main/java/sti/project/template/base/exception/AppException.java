package sti.project.template.base.exception;

import lombok.Getter;

/**
 * Application exception with error code support.
 * Use this for all business logic exceptions.
 */
@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] messageArgs;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.messageArgs = null;
    }

    public AppException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.messageArgs = args;
    }

    public AppException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.messageArgs = null;
    }

    public boolean hasMessageArgs() {
        return messageArgs != null && messageArgs.length > 0;
    }
}
