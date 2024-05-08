package client;

import common.Controllers.PropertiesFilesController;
import common.Exceptions.*;
import common.Exceptions.authorization.AuthorizationException;
import common.Exceptions.authorization.DifferentPasswordsException;
import common.Exceptions.authorization.EmptyUsernameException;
import common.UI.Console;
import common.UI.YesNoQuestionAsker;
import common.net.dataTransfer.UserInfo;
import common.net.requests.ClientRequest;
import common.net.requests.ClientRequestType;
import common.net.requests.ResultState;
import common.net.requests.ServerResponse;
import common.utils.PasswordHasher;
import common.utils.RandomStringGenerator;

import java.io.IOException;
import java.util.Properties;

/**
 * Class to control authorization of user
 */
public class AuthorizationController {
    /**
     * Method to handle server response after sending authorization request
     * @throws ReceivingDataException If an error occurred while receiving data
     * @throws AuthorizationException If authorization was not successful
     */
    private static void handleAuthorizationResult() throws ReceivingDataException, AuthorizationException {
        ServerResponse response = (ServerResponse) UDPClient.getInstance().receiveObject();
        if(response.state() == ResultState.EXCEPTION){
            throw (AuthorizationException) response.data();
        }
    }

    /**
     * Method to complete user log in
     * <p>After reading user's password method loads personal user's pepper, add it to password and then hash it
     * @throws SendingDataException If an error occurred while sending data to server
     * @throws ReceivingDataException If an error occurred while receiving data from server
     * @throws AuthorizationException If authorization was not successful
     * @throws IOException If an error occurred while reading pepper from property file
     */
    public static UserInfo logIn(String userName, String password) throws SendingDataException, ReceivingDataException, AuthorizationException, ConfigurationFileIOException {
        String pepper = null;
        try {
            pepper = new PropertiesFilesController().readProperties(String.format("%s_pepper.properties", userName)).getProperty("pepper");
        } catch (IOException e) {
            throw new ConfigurationFileIOException();
        }

        String hashedPassword = new PasswordHasher().get_SHA_512_SecurePassword(password + pepper);

        UserInfo userInfo = new UserInfo(userName, hashedPassword);
        UDPClient.getInstance().sendObject(
                new ClientRequest(ClientRequestType.LOG_IN, userInfo));
        handleAuthorizationResult();
        return userInfo;
    }

    /**
     * Method to complete registration of new user
     * <p>After reading username it checks if it is valid by sending data to server
     * <p>After reading password, random string with pepper is generated and saved in property file
     * <p>Password is sent to server as hash
     * @throws SendingDataException If an error occurred while sending data to server
     * @throws ReceivingDataException If an error occurred while receiving data from server
     * @throws AuthorizationException If authorization was not successful
     * @throws IOException If an error occurred while saving pepper to property file
     */
    public static void singUp(String userName, String password, String confirmedPassword) throws SendingDataException, ReceivingDataException, AuthorizationException, ConfigurationFileIOException {
        if(userName.isEmpty()){
            throw new EmptyUsernameException();
        }

        UDPClient.getInstance().sendObject(
                new ClientRequest(ClientRequestType.CHECK_USERNAME, userName));
        handleAuthorizationResult();

        if(!password.equals(confirmedPassword)){
            throw new DifferentPasswordsException();
        }

        String pepper = new RandomStringGenerator().generate();
        Properties pepperProperties = new Properties();
        pepperProperties.setProperty("pepper", pepper);

        try {
            new PropertiesFilesController().
                    writeProperties(pepperProperties, String.format("%s_pepper.properties", userName), "pepper");
        } catch (IOException e) {
            throw new ConfigurationFileIOException();
        }

        String hashedPassword = new PasswordHasher().get_SHA_512_SecurePassword(password + pepper);

        UDPClient.getInstance().sendObject(
                new ClientRequest(ClientRequestType.SIGN_IN, new UserInfo(userName, hashedPassword)));
        handleAuthorizationResult();
    }
}
