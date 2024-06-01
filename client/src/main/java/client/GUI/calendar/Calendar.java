package client.GUI.calendar;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import java.util.Properties;

/**
 * Class of calendar swing component
 */
public class Calendar extends JDatePickerImpl {
    private static final Properties p = new Properties();

    static {
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
    }
    public Calendar() {
        super(new JDatePanelImpl(new SqlDateModel(), p), new DateLabelFormatter());
    }
}
