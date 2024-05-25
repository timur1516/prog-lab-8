package client.Exceptions;

import common.Exceptions.LocalizedException;

public class ScriptFileReadingException extends LocalizedException {
    public ScriptFileReadingException() {
        super("scriptFileReadingException");
    }
}
