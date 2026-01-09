package sti.project.template.base.exception;

import java.util.UUID;

/**
 * Exception thrown when a resource is not found.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entityName, UUID id) {
        super(String.format("%s with id '%s' not found", entityName, id));
    }

    public NotFoundException(Class<?> entityClass, UUID id) {
        super(String.format("%s with id '%s' not found", entityClass.getSimpleName(), id));
    }
}
