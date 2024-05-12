package client.Commands;

import client.net.UDPClient;
import common.Commands.UserCommand;
import common.net.dataTransfer.PackedCommand;
import common.net.requests.*;

import java.util.ArrayList;

/**
 * Class with realization of print_field_descending_salary command for client
 * <p>This command is used to print values of all salary fields in collection in descending order
 * @see UserCommand
 */
public class PrintFieldDescendingSalaryCommand extends ClientCommand {
    /**
     * PrintFieldDescendingSalaryCommand constructor
     * <p> Firstly it initializes super constructor by command name, arguments and description
     */
    public PrintFieldDescendingSalaryCommand() {
        super("print_field_descending_salary", "print values of all salary fields in collection in descending order");
    }

    /**
     * Method to complete print_field_descending_salary command
     * <p>It prints list of all salaries in descending order
     * <p>If collection is empty user is informed
     */
    @Override
    public ServerResponse execute() {
        return sendAndReceive();
    }
}
