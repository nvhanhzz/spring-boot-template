package sti.project.template.base.exception;

/**
 * Exception for forbidden access errors (403).
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("You do not have permission to access this resource");
    }
}
