package client.Readers;

import client.GUI.calendar.Calendar;
import client.Parsers.Parser;
import common.Exceptions.InvalidDataException;
import common.Validators.Validator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Class to read and validate value from {@link JComboBox}
 * <p>If values is valid, green frame appears
 * <p>If values is wrong, red frame appears and error dialog is shown
 */
public class ComboBoxReader {
    public static <T> T readValue(JComboBox<String> comboBox, Validator<T> validator, Parser<T> parser) throws InvalidDataException {
        comboBox.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED));
        T value;
        String text = (String) comboBox.getSelectedItem();
        value = text.isEmpty() ? null : parser.parse(text);
        validator.validate(value);
        comboBox.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GREEN, Color.GREEN));
        return value;
    }
}
