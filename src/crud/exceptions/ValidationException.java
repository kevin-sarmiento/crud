package crud.exceptions;

public class ValidationException extends RepositoryException {
    public ValidationException(String message) {
        super(message);
    }
}
