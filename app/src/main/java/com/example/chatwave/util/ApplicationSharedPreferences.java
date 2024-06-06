package com.example.chatwave.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ApplicationSharedPreferences {
    private static final String APPLICATION_PREFERENCES_NAME = "UserAuthPrefs";

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
    public static Object getSavedObject(String key, Object default_value, Class<?> className, Context context) {

        Object retrievedObj = null;
        SharedPreferences preferences = context.getSharedPreferences(APPLICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);

        String retStr = preferences.getString(key, "");

        if (retStr == null || retStr.isEmpty()) {
            retrievedObj = default_value;
        } else {

            Gson gson = new Gson();
            try {
                retrievedObj = gson.fromJson(retStr, className);

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                retrievedObj = default_value;
            }
        }
        return retrievedObj;
    }

}
