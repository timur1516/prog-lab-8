package common.Exceptions.authorization;

/**
 * Exception which is thrown if username was not found in server database
 */
public class UsernameNotFoundException extends AuthorizationException {
    public UsernameNotFoundException(String username) {
        super("usernameNotFoundException", username);
    }
}
