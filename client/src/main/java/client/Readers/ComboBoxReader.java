package client.Readers;

import client.Parsers.Parser;
import common.Exceptions.InvalidDataException;
import common.Validators.Validator;

import javax.swing.*;
import java.awt.*;

public class ComboBoxReader {
    public static <T> T readValue(JComboBox<String> comboBox, String valueName, Validator<T> validator, Parser<T> parser) throws InvalidDataException {
        comboBox.setBackground(Color.RED);
        T value;
        String text = (String) comboBox.getSelectedItem();
        value = text.isEmpty() ? null : parser.parse(text);
        validator.validate(value);
        comboBox.setBackground(Color.GREEN);;
        return value;
    }
}
