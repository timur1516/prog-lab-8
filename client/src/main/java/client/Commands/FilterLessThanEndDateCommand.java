package client.Commands;

import client.Readers.WorkerReader;
import client.net.UDPClient;
import client.Constants;
import common.Commands.UserCommand;
import common.Exceptions.InvalidDataException;
import common.net.dataTransfer.PackedCommand;
import common.net.requests.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class with realization of filter_less_than_end_date command for client
 * <p>This command is used to print all elements whose endDate is less than given
 * @see ClientCommand
 */
public class FilterLessThanEndDateCommand extends ClientCommand {
    private LocalDateTime endDate;

    /**
     * FilterLessThanEndDateCommand constructor
     * <p> Firstly it initializes super constructor by command name, arguments and description
     */
    public FilterLessThanEndDateCommand() {
        super("filter_less_than_end_date", "print all elements whose endDate is less than given","{endDate}");
    }

    /**
     * FilterLessThanEndDateCommand constructor with arguments
     * @param endDate
     */
    public FilterLessThanEndDateCommand(LocalDateTime endDate) {
        super("filter_less_than_end_date", "print all elements whose endDate is less than given","{endDate}");
        this.endDate = endDate;
    }

    /**
     * Method to complete filter_less_than_end_date command
     * <p>It reads endDate from user and then print all elements whose endDate is less than given
     * <p>If collection is empty endDate is not read (except script mode)
     *
     */
    @Override
    public ServerResponse execute() {
        arguments.add(endDate);
        return sendAndReceive();
    }

    @Override
    public void readData() throws InvalidDataException {
        endDate = WorkerReader.getInstance().readEndDate();
    }
}
