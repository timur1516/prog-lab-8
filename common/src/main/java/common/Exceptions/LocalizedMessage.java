package common.Exceptions;

import java.io.Serializable;

public class LocalizedMessage implements Serializable {
    private final String messageKey;
    private final Object[] arguments;

    public LocalizedMessage(String messageKey, Object ... arguments){
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
