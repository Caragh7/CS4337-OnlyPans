package onlypans.common.exceptions;

public class UnableToDeleteResourceException extends RuntimeException {
    public UnableToDeleteResourceException(String message, Exception e) {
        super(message, e);
    }
}
