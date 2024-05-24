package common.Exceptions.authorization;

/**
 * Exception which is thrown if user entered empty username in process of registration
 */
public class EmptyUsernameException extends AuthorizationException{
    public EmptyUsernameException(){
        super("emptyUsernameException");
    }
}
