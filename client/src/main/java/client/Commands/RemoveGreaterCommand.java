package client.Commands;

import client.Readers.WorkerReader;
import client.net.UDPClient;
import common.Collection.Worker;
import client.Constants;
import common.Commands.UserCommand;
import common.Exceptions.InvalidDataException;
import common.net.dataTransfer.PackedCommand;
import common.net.requests.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class with realization of remove_greater command for client
 * <p>This command is used to remove all elements which are greater than given
 * @see UserCommand
 */
public class RemoveGreaterCommand extends ClientCommand {
    private Worker worker;
    /**
     * RemoveGreaterCommand constructor
     * <p> Firstly it initializes super constructor by command name, arguments and description
     */
    public RemoveGreaterCommand() {
        super("remove_greater", "remove all elements which are greater than given", "{element}");
    }
    public RemoveGreaterCommand(Worker worker) {
        super("remove_greater", "remove all elements which are greater than given", "{element}");
        this.worker = worker;
    }

    /**
     * Method to complete remove_greater command
     * <p>It reads element to compare with and then removes elements which are greater that it
     * <p>In the end it prints number of deleted elements
     * <p>If collection is empty element is not read (except script mode)
     *
     */
    @Override
    public ServerResponse execute() {
        arguments.add(worker);
        arguments.add(ClientRequest.getUser().userName());
        return sendAndReceive();
    }

    @Override
    public void readData() throws InvalidDataException {
        worker = WorkerReader.getInstance().readWorker();
    }
}
