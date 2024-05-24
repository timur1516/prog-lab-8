package client.Exceptions;

import common.Exceptions.LocalizedException;

/**
 * Signals that script is recursive
 */
public class RecursiveScriptException extends LocalizedException {
    public RecursiveScriptException(){
        super("recursiveScriptException");
    }
}
