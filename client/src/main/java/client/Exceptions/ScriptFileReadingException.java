package client.Exceptions;

import common.Exceptions.LocalizedException;

/**
 * Signals that some error occurred while reading script file
 */
public class ScriptFileReadingException extends LocalizedException {
    public ScriptFileReadingException() {
        super("scriptFileReadingException");
    }
}
