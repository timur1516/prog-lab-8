package common.Exceptions;

/**
 * Exception which is thrown if an error occurred in process of receiving data from
 */
public class ReceivingDataException extends LocalizedException{
    public ReceivingDataException(String messageKey, Object ... arguments){
        super(messageKey, arguments);
    }
}
