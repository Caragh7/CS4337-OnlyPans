package onlypans.common.exceptions;

public class UnableToCreateResourceException extends RuntimeException {
    public UnableToCreateResourceException(String message, Exception e) {
        super(message, e);
    }
}
