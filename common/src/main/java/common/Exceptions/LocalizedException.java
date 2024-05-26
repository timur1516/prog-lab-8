package common.Exceptions;

import javax.swing.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class LocalizedException extends Exception implements Serializable {
    private final String messageKey;
    private final Object[] arguments;

    public LocalizedException(String messageKey, Object ... arguments){
        this.messageKey = messageKey;
        this.arguments = arguments;
    }

   public String getMessage(ResourceBundle bundle){
       return MessageFormat.format(bundle.getString(messageKey), arguments);
   }
}
