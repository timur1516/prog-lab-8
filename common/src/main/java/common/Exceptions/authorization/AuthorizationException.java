package common.Exceptions.authorization;

import common.Exceptions.LocalizedException;

/**
 * Super class for all exception which can be caused while authorization
 */
public class AuthorizationException extends LocalizedException {
    public AuthorizationException(String messageKey, Object ... arguments){
        super(messageKey, arguments);
    }
}