package com.cedro.musicplayer;

import java.util.ResourceBundle;

public class Localization {
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("com.cedro.musicplayer.strings");

    public static String getString(String key) {
        return BUNDLE.getString(key);
    }
}
