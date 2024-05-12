package client.Exceptions;

import common.Exceptions.InvalidDataException;

public class ValueParsingException extends Exception {
    public ValueParsingException(String message){
        super(message);
    }
}
