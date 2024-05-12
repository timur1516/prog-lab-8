package client.Readers;

import client.Exceptions.ValueParsingException;
import client.Parsers.Parser;
import common.Exceptions.InvalidDataException;
import common.Validators.Validator;

import javax.swing.*;
import java.awt.*;

public class TextFieldReader {
    public static <T> T readValue(JTextField textField, String valueName, Validator<T> validator, Parser<T> parser) throws ValueParsingException, InvalidDataException {
        textField.setBackground(Color.RED);
        T value;
        try {
            value = textField.getText().trim().isEmpty() ? null : parser.parse(textField.getText().trim());
        } catch (InvalidDataException e) {
            throw new ValueParsingException(valueName + ": " + e.getMessage());
        }
        validator.validate(value);
        textField.setBackground(Color.GREEN);
        return value;
    }
}
