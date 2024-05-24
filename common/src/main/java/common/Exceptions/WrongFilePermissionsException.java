package common.Exceptions;

import java.io.FileNotFoundException;

/**
 * Signals that file has wrong permissions
 */
public class WrongFilePermissionsException extends LocalizedException {
    public WrongFilePermissionsException(String messageKey, Object ... arguments){
        super(messageKey, arguments);
    }
}
