package client.Readers;

import client.GUI.calendar.Calendar;
import common.Exceptions.InvalidDataException;
import common.Validators.Validator;

import java.time.LocalDateTime;
import java.sql.Date;

public class CalendarReader {
    public static LocalDateTime readValue(Calendar calendar, Validator<LocalDateTime> validator) throws InvalidDataException {
        LocalDateTime value;
        Date date = (Date) calendar.getModel().getValue();
        value = date == null ? null : date.toLocalDate().atStartOfDay();
        validator.validate(value);
        return value;
    }
}
