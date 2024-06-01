package client.Readers;

import client.GUI.calendar.Calendar;
import client.Parsers.Parser;
import common.Exceptions.InvalidDataException;
import common.Validators.Validator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Class to read and validate value from {@link JTextField}
 * <p>If values is valid, green frame appears
 * <p>If values is wrong, red frame appears and error dialog is shown
 */
public class TextFieldReader {
    public static <T> T readValue(JTextField textField, Validator<T> validator, Parser<T> parser) throws InvalidDataException {
        textField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED));
        T value;
        value = textField.getText().trim().isEmpty() ? null : parser.parse(textField.getText().trim());
        validator.validate(value);
        textField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GREEN, Color.GREEN));
        return value;
    }
}
