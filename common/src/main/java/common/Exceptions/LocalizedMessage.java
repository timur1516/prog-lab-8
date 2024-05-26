package common.Exceptions;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class LocalizedMessage implements Serializable {
    private final String messageKey;
    private final Object[] arguments;

    public LocalizedMessage(String messageKey, Object ... arguments){
        this.messageKey = messageKey;
        this.arguments = arguments;
    }

    public String getMessage(ResourceBundle bundle){
        return MessageFormat.format(bundle.getString(messageKey), arguments);
    }
}
