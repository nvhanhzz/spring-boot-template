package sti.project.template.base.exception;

import lombok.Getter;
import sti.project.scada.base.dto.FieldErrorDetail;

import java.util.List;

/**
 * Exception for validation errors with field details.
 */
@Getter
public class ValidationException extends RuntimeException {
    private final List<FieldErrorDetail> fieldErrors;

    public ValidationException(String message, List<FieldErrorDetail> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(List<FieldErrorDetail> fieldErrors) {
        super("Validation failed");
        this.fieldErrors = fieldErrors;
    }
}
