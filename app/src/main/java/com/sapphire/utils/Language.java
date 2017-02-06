package com.sapphire.utils;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import com.sapphire.Sapphire;
import java.util.Locale;

public class Language {
    public static void translate() {
        SharedPreferences sPref = Sapphire.getInstance().getSharedPreferences("GlobalPreferences", Sapphire.getInstance().MODE_PRIVATE);
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
        Sapphire.getInstance().getBaseContext().getResources().updateConfiguration(config,
                Sapphire.getInstance().getBaseContext().getResources().getDisplayMetrics());
    }
}