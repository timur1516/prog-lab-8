package client.Commands;

import client.net.UDPClient;

import common.Commands.UserCommand;
import common.Exceptions.ReceivingDataException;
import common.Exceptions.SendingDataException;
import common.net.dataTransfer.PackedCommand;
import common.net.requests.ClientRequest;
import common.net.requests.ClientRequestType;
import common.net.requests.ResultState;
import common.net.requests.ServerResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class which adds additional methods to {@link common.Commands.UserCommand} class
 * <p>It makes communication with server easier
 */
public abstract class ClientCommand extends UserCommand {
    /**
     * Command arguments which will be sent to server
     */
    protected ArrayList<Serializable> arguments;

    /**
     * UserCommand constructor for commands with any number of arguments
     *
     * @param name
     * @param description
     * @param argumentsDescription
     */
    public ClientCommand(String name, String description, String... argumentsDescription) {
        super(name, description, argumentsDescription);
        arguments = new ArrayList<>();
    }

    /**
     * Method to send request to server and get response
     * @return Response from server
     */
    protected ServerResponse sendAndReceive(){
        try {
            return UDPClient.getInstance()
                    .sendAndReceive(
                            new ClientRequest(ClientRequestType.EXECUTE_COMMAND, new PackedCommand(super.getName(), arguments)));
        } catch (ReceivingDataException | SendingDataException e) {
            return new ServerResponse(ResultState.EXCEPTION, e);
        }
    }
}
