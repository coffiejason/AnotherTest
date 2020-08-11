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

    private static final String PROFILE_SHARED_PREFS = "profilesharedpref";
    private static final String USERNAME_KEY = "username";
    private static final String ID_NUMBER_KEY = "id_number";
    private static final String PHONE_NUMBER_KEY = "phone_number";


    private SharedPrefs(Context context){
        ctx = context;
    }

    static synchronized SharedPrefs getInstance(Context ctx){
        if(mInstance == null){
            mInstance = new SharedPrefs(ctx);
        }
        return mInstance;
    }
    //fcm token
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

    //login
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

    //profile
    static void setUsername(String username){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY,username);
        editor.apply();
    }

    static String getUsername(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME_KEY,"Username");
    }

    static void setPhonenum(String phonenum){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_NUMBER_KEY,phonenum);
        editor.apply();
    }

    static String getPhonenum(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PHONE_NUMBER_KEY,"+000 000 0000");
    }

    static void setIDnumber(String IDnumber){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_NUMBER_KEY,IDnumber);
        editor.apply();
    }

    static String getIDnumber(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_NUMBER_KEY,null);
    }
}
