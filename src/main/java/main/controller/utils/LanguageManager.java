package main.controller.utils;

import main.model.AbstractObserver;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager extends AbstractObserver {
    private static LanguageManager instance;
    private String currentLanguage;
    private Locale currentLocale;
    private ResourceBundle bundle;

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setLanguage(String language) {
        this.currentLanguage = language;
        notifyObservers();
    }

    public LanguageManager() {
        setLocale(Locale.ENGLISH);  //default language
    }

    public void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("main.Labels", currentLocale);
        notifyObservers();
    }

    public String getText(String key) {
        return bundle.getString(key);
    }
}