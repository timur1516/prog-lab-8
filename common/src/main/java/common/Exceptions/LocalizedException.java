package common.Exceptions;

import java.util.Objects;

public class LocalizedException extends Exception{
    private final String messageKey;
    private final Object[] arguments;

    public LocalizedException(String messageKey, Object ... arguments){
        this.messageKey = messageKey;
        this.arguments = arguments;
    }

    public String getMessageKey(){
        return this.messageKey;
    }
    public Object[] getArguments(){
        return this.arguments;
    }
}
