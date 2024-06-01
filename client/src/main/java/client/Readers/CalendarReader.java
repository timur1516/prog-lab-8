package client.Readers;

import client.GUI.calendar.Calendar;
import common.Exceptions.InvalidDataException;
import common.Validators.Validator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.sql.Date;

/**
 * Class to read and validate value from {@link Calendar}
 * <p>If values is valid, green frame appears
 * <p>If values is wrong, red frame appears and error dialog is shown
 */
public class CalendarReader {
    public static LocalDateTime readValue(Calendar calendar, Validator<LocalDateTime> validator) throws InvalidDataException {
        calendar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED));
        LocalDateTime value;
        Date date = (Date) calendar.getModel().getValue();
        value = date == null ? null : date.toLocalDate().atStartOfDay();
        validator.validate(value);
        calendar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GREEN, Color.GREEN));
        return value;
    }
}
