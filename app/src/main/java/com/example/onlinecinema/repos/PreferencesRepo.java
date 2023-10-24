package com.example.onlinecinema.repos;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesRepo {

    public static final String APP_PREFERENCES = "APP_PREFERENCES";

    private SharedPreferences preferences;

    public PreferencesRepo(Context context) {
        this.preferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
    }

    public void save(String jsonString, String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, jsonString);
        editor.commit();
    }

    public void save(Boolean value, String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String get(String key) {
        return preferences.getString(key, "");
    }

    public Boolean getBoolean(String key) {
        return preferences.getBoolean(key, true);
    }
}
