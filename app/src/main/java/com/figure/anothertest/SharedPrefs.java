package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class SharedPrefs {

    @SuppressLint("StaticFieldLeak")
    private static Context ctx;
    @SuppressLint("StaticFieldLeak")
    private static SharedPrefs mInstance;

    private static final String SHARED_PREF_NAME = "fcmsharedpref";
    private static final String KEY_ACCESS_TOKEN = "token";

    private SharedPrefs(Context context){
        ctx = context;
    }

    static synchronized SharedPrefs getInstance(Context ctx){
        if(mInstance == null){
            mInstance = new SharedPrefs(ctx);
        }
        return mInstance;
    }

    void storeToken(String token){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
    }

    String getToken(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);
    }

    static void setDefaults(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Loggedin", "true");
        editor.apply();
    }

    static String getDefaults(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("Loggedin", null);
    }
}
