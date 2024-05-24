package common.Exceptions;

/**
 * Exception which is sent to client if any error occurred on server
 */
public class ServerErrorException extends LocalizedException{
    public ServerErrorException(){
        super("serverErrorException");
    }
}
