package client.Controllers;

import common.Controllers.PropertiesFilesController;

import java.io.IOException;
import java.util.*;

public class LocaleController {
    private static LocaleController LOCALE_CONTROLLER = null;
    public static LocaleController getInstance(){
        if(LOCALE_CONTROLLER == null){
            LOCALE_CONTROLLER = new LocaleController();
        }
        return LOCALE_CONTROLLER;
    }

    private final List<String> localeNames;
    private final List<Locale> locales;
    private Locale currentLocale;

    private LocaleController() {
        locales = new ArrayList<>();
        localeNames = new ArrayList<>();
        currentLocale = Locale.ENGLISH;
        try {
            loadLocales();
        } catch (IOException ignored) {}

    }

    private void loadLocales() throws IOException {
        Properties properties = new PropertiesFilesController().readProperties("locales.properties");
        properties.forEach((key, value) -> {
            locales.add(new Locale((String) key));
            localeNames.add((String) value);
        });
    }

    public List<String> getLocaleNames(){
        return localeNames;
    }

    public Locale getCurrentLocale(){
        return currentLocale;
    }

    public int getCurrentLocaleNumber(){
        return locales.indexOf(currentLocale);
    }

    public void setLocale(int index){
        currentLocale = locales.get(index);
    }
}
