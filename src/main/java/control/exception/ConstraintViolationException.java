package control.exception;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("CONSTRAINT_VIOLATION")
public class ConstraintViolationException extends RuntimeException implements ShowErrorException {
    public ConstraintViolationException(String message) {
        super(message);
    }
}