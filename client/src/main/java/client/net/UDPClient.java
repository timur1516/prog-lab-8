package client.net;

import common.Exceptions.ReceivingDataException;
import common.Exceptions.SendingDataException;
import common.net.requests.ClientRequest;
import common.net.requests.ServerResponse;
import common.utils.Serializer;

import java.io.*;
import java.net.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static common.utils.CommonConstants.PACKET_SIZE;

/**
 * Singleton class for UPD client
 */
public class UDPClient {
    private static UDPClient UDP_CLIENT = null;
    private final Lock lock;
    /**
     * Datagram socket for client
     */
    DatagramSocket ds;
    /**
     * Servers host
     */
    InetAddress host;
    /**
     * Servers port
     */
    int port;
    /**
     * Timeout for waiting for server response
     */
    int timeout;

    private UDPClient(){
        lock = new ReentrantLock();
    };
    public static synchronized UDPClient getInstance(){
        if(UDP_CLIENT == null){
            UDP_CLIENT = new UDPClient();
        }
        return UDP_CLIENT;
    }

    /**
     * Method to init client
     * @param host
     * @param port
     * @param timeout
     */
    public void init(InetAddress host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * Method to start UDP client
     * @throws SocketException If any error occurred
     */
    public void open() throws SocketException {
        this.ds = new DatagramSocket();
        this.ds.setSoTimeout(this.timeout);
    }

    /**
     * Method to stop UPD client
     */
    public void stop() {
        this.ds.close();
    }

    /**
     * Method to receive Serializable object
     * @return Object which was received
     * @throws ReceivingDataException If any error while receiving data was occurred
     */
    private Serializable receiveObject() throws ReceivingDataException {
        try {
            byte arr[] = new byte[PACKET_SIZE];
            DatagramPacket dp = new DatagramPacket(arr, PACKET_SIZE);
            this.ds.receive(dp);
            return Serializer.deserialize(arr);
        }
        catch (Exception e){
            throw new ReceivingDataException("receivingDataException", "server");
        }
    }

    /**
     * Method to send Serializable object
     * @param o Object to send
     * @throws SendingDataException If any error occurred while sending data
     */
    private void sendObject(Serializable o) throws SendingDataException {
        try {
            byte arr[] = Serializer.serialize(o);
            DatagramPacket dp = new DatagramPacket(arr, arr.length, this.host, this.port);
            this.ds.send(dp);
        }
        catch (Exception e){
            throw new SendingDataException();
        }
    }

    public ServerResponse sendAndReceive(ClientRequest request) throws SendingDataException, ReceivingDataException {
        lock.lock();
        try {
            sendObject(request);
            return (ServerResponse) receiveObject();
        } catch (SendingDataException | ReceivingDataException e){
            throw e;
        } finally {
            lock.unlock();
        }
    }
}
