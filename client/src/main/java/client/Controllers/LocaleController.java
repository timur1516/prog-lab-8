package client.Controllers;

import common.Controllers.PropertiesFilesController;

import java.io.IOException;
import java.util.*;

/**
 * Singleton class to control locales
 */
public class LocaleController {
    private static LocaleController LOCALE_CONTROLLER = null;
    public static LocaleController getInstance(){
        if(LOCALE_CONTROLLER == null){
            LOCALE_CONTROLLER = new LocaleController();
        }
        return LOCALE_CONTROLLER;
    }

    /**
     * List of names of available locales
     * <p>It contains localized names like 'English'
     */
    private final List<String> localeNames;
    /**
     * List of available locales as Locale objects
     */
    private final List<Locale> locales;
    /**
     * Current locale
     */
    private Locale currentLocale;

    /**
     * Locale controller constructor
     * <p>It try to load available locales from file
     * <p>If it was not successful, locale is set to default value: en
     */
    private LocaleController() {
        locales = new ArrayList<>();
        localeNames = new ArrayList<>();
        currentLocale = Locale.ENGLISH;
        try {
            loadLocales();
        } catch (IOException ignored) {}

    }

    /**
     * Method to load liat of available locales with their names from property file
     * @throws IOException If any i\o error occurred
     */
    private void loadLocales() throws IOException {
        Properties properties = new PropertiesFilesController().readResource("locales.properties");
        properties.forEach((key, value) -> {
            locales.add(new Locale((String) key));
            localeNames.add((String) value);
        });
    }

    /**
     * Method to get names of all available locales
     * @return
     */
    public List<String> getLocaleNames(){
        return localeNames;
    }

    /**
     * Method to get current locale
     * @return
     */
    public Locale getCurrentLocale(){
        return currentLocale;
    }

    /**
     * Method to get number of current locale in list of locales (used for correct comboBox processing)
     * @return
     */
    public int getCurrentLocaleNumber(){
        return locales.indexOf(currentLocale);
    }

    /**
     * Method to get locale by it index in list of locales
     * @param index
     */
    public void setLocale(int index){
        currentLocale = locales.get(index);
    }
}
