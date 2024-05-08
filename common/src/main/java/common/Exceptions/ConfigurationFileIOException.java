package common.Exceptions;

import java.io.IOException;

public class ConfigurationFileIOException extends IOException {
    public ConfigurationFileIOException(){
        super("Error while working with configuration files");
    }
}
