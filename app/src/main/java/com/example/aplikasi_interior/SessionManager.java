package com.example.aplikasi_interior;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MyAppPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    public static final String KEY_LOGGED_IN = "logged_in";
    public static final String KEY_USER_ID = "user_id";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(String username, String userId) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_ID, userId);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put(KEY_USERNAME, sharedPreferences.getString(KEY_USERNAME, ""));
        userDetails.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, ""));
        userDetails.put(KEY_USER_ID, sharedPreferences.getString(KEY_USER_ID, ""));
        return userDetails;
    }
    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }
    public void clearSession() {
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_LOGGED_IN);
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}
