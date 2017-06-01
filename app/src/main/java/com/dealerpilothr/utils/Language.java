package com.dealerpilothr.utils;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import com.dealerpilothr.Dealerpilothr;
import java.util.Locale;

public class Language {
    public static void translate() {
        SharedPreferences sPref = Dealerpilothr.getInstance().getSharedPreferences("GlobalPreferences", Dealerpilothr.getInstance().MODE_PRIVATE);
        String languageToLoad  = sPref.getString("LANGUAGE",""); // your language
        if (languageToLoad.equals("")) {
            String defaultLanguage = Locale.getDefault().getLanguage();
            if (",fr,".indexOf(defaultLanguage) != -1) {
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("LANGUAGE", defaultLanguage);
                ed.apply();
            } else {
                return;
            }
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Dealerpilothr.getInstance().getBaseContext().getResources().updateConfiguration(config,
                Dealerpilothr.getInstance().getBaseContext().getResources().getDisplayMetrics());
    }
}