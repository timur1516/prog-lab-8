package common.Exceptions;

/**
 * Exception which is thrown if client tried to modify element of collection which does not belong to him
 */
public class AccessDeniedException extends LocalizedException {
    public AccessDeniedException(String messageKey){
        super(messageKey);
    }
}