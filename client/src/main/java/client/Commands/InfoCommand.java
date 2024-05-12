package client.Commands;

import client.net.UDPClient;
import common.Commands.UserCommand;
import common.net.dataTransfer.PackedCommand;
import common.net.requests.*;

import java.util.ArrayList;

/**
 * Class with realization of info command for client
 * <p>This command is used to print information about collection
 * @see UserCommand
 */
public class InfoCommand extends ClientCommand {
    /**
     * InfoCommand constructor
     * <p> Firstly it initializes super constructor by command name, arguments and description
     */
    public InfoCommand() {
        super("info", "print information about collection");
    }

    /**
     * Method to complete info command
     * <p>It prints info from collection controller
     */
    @Override
    public ServerResponse execute() {
        return sendAndReceive();
    }
}
