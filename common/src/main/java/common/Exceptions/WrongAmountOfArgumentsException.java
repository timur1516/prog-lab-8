package common.Exceptions;

/**
 * Signals that command got wrong amount of arguments
 */
public class WrongAmountOfArgumentsException extends LocalizedException{
    /**
     * Constructs an {@code WrongAmountOfArgumentsException} with the specified detail message and additional data
     *
     * @param commandName
     *        The name of command
     * @param expectedArguments Expected number of arguments
     * @param givenArguments Given number of arguments
     */
    public WrongAmountOfArgumentsException(String commandName, int expectedArguments, int givenArguments){
        super("wrongAmountOfArgumentsException", commandName, expectedArguments, givenArguments);
    }
}
