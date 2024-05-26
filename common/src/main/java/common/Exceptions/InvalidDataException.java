package common.Exceptions;

/**
 * Signals that some data is not valid
 */
public class InvalidDataException extends LocalizedException {
    public InvalidDataException(String messageKey, Object ... arguments){
        super(messageKey, arguments);
    }
}