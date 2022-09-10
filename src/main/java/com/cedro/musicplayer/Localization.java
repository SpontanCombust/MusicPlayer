package com.cedro.musicplayer;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Small utility class used to retrieve localised strings
 */
public class Localization {
    /**
     * ResourceBundle with the localised strings for the application, suited for the default locale of the system
     */
    public static final ResourceBundle BUNDLE;

    static {
        if(Locale.getDefault().getLanguage() != new Locale("pl", "PL").getLanguage()) {
            Locale.setDefault(Locale.ENGLISH);
        }

        BUNDLE = ResourceBundle.getBundle("com.cedro.musicplayer.strings");
    }

    /**
     * Returns the localised string
     * 
     * @param key - string key
     * @return String - localised string
     */
    public static String getString(String key) {
        return BUNDLE.getString(key);
    }
}
