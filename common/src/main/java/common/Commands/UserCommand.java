package common.Commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import common.Exceptions.*;

/**
 * Abstract class for all commands from user
 */
public abstract class UserCommand implements ICommand {
    /**
     * Command name
     */
    private final String name;
    /**
     * Command arguments as one String
     */
    private final String[] argumentsDescription;
    /**
     * Amount of argument which is calculated in constructor
     */
    private int amountOfArguments;
    /**
     * Command description
     */
    private final String description;

    /**
     * UserCommand constructor for commands with any number of arguments
     * @param name
     * @param description
     */
    public UserCommand(String name, String description, String ... argumentsDescription){
        this.name = name;
        this.argumentsDescription = argumentsDescription;
        this.description = description;
        countArgs();
    }

    /**
     * Method to count number of arguments
     * <p>It excludes all data arguments which won't be written in the same line with command name
     */
    private void countArgs(){
        int cnt = 0;
        for(String arg : this.argumentsDescription){
            if(!arg.contains("{")) cnt += 1;
        }
        this.amountOfArguments = cnt;
    }

    /**
     * Method to get command name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Method to check and load arguments of command
     *
     * @param arguments String array with different arguments
     * @throws WrongAmountOfArgumentsException If number of arguments is wrong for given command
     * @throws InvalidDataException            if command arguments are not valid
     */
    public void initCommandArgs(ArrayList<Serializable> arguments) throws InvalidDataException, WrongAmountOfArgumentsException {
        if(this.amountOfArguments != arguments.size()){
            throw new WrongAmountOfArgumentsException("Wrong amount of arguments for command "
                    + this.name + "!", this.amountOfArguments, arguments.size());
        }
    }

    /**
     * Method to read additional data for command
     * <p>May be override if necessary
     */
    public void readData() throws InvalidDataException{}


    /**
     * Method to get String representation of command
     * @return String with command name, arguments and description
     */
    @Override
    public String toString() {
        String res = this.name;
        for(String arg : this.argumentsDescription){
            res += " " + arg;
        }
        res += ": " + this.description;
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCommand command)) return false;
        return Objects.equals(name, command.name) && Objects.equals(argumentsDescription, command.argumentsDescription) && Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, argumentsDescription, description);
    }
}
