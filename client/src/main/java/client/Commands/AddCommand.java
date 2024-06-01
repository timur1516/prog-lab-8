package client.Commands;

import client.Readers.WorkerReader;
import client.net.UDPClient;
import common.Collection.Worker;
import common.Commands.UserCommand;
import common.Exceptions.InvalidDataException;
import common.net.dataTransfer.PackedCommand;
import common.net.requests.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class with realization of add command on client app
 * <p>This command is used to read data from user and send request to server
 * @see UserCommand
 */
public class AddCommand extends ClientCommand {
    private Worker worker;

    /**
     * Constructor for add command without arguments
     */
    public AddCommand() {
        super("add", "add new element to collection", "{element}");
    }

    /**
     * Constructor for add command with worker to add
     * @param worker
     */
    public AddCommand(Worker worker) {
        super("add", "add new element to collection", "{element}");
        this.worker = worker;
    }

    /**
     * Method to complete add command
     * <p>It creates request with element to add and send it to server
     * @return Response from server
     */
    @Override
    public ServerResponse execute() {
        arguments.add(worker);
        arguments.add(ClientRequest.getUser().userName());
        return sendAndReceive();
    }

    @Override
    public void readData() throws InvalidDataException {
        this.worker = WorkerReader.getInstance().readWorker();
    }
}
