package com.example.chatwave.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ApplicationSharedPreferences {

    public static final String APPLICATION_PREFERENCES_NAME = "ChatWave";
    public static void set(String key, boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APPLICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static void saveObject(String key, Object value, Context context) {

        if (value == null) {
            return;
        }
        Gson gson = new Gson();
        String json = gson.toJson(value);

        SharedPreferences preferences = context.getSharedPreferences(APPLICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, json);
        edit.apply();
    }
}
