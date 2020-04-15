package fr.modcraftmc.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Message {

    public static ResourceBundle rb = ResourceBundle.getBundle("lang.messages", new Locale(getLanguage(), getCountry()), new UTF8Control());

    public static String getString(String key) {
        return rb.getString(key);
    }


    public static String getLocale(int key) {
        //TODO: get locale form launcher options
        String locale = "fr_FR";
        if (locale.length() == 5) {
            String language = locale.substring(0, 2);
            String country = locale.substring(3, 5);
            if(key == 1) return language;
            if(key == 2) return country;
        }
        return "xx";
    }

    public static String getLanguage() {
        return getLocale(1);
    }

    public static String getCountry() {
        return getLocale(2);
    }

    public static String getLocale() {
        return Message.rb.getLocale().toString();
    }
}
