package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

    //UtilER
    private static final String UTIL_SHARED_NAME = "utilsharedpref";

    private static final String TASK_ID = "taskid";

    private static final String METER_NUM_KEY0 = "meternum0";
    private static final String READING_KEY0 = "reading0";
    private static final String PICTURE_READ_KEY0 = "picread0";

    private static final String METER_NUM_KEY1 = "meternum1";
    private static final String READING_KEY1 = "reading1";
    private static final String PICTURE_READ_KEY1 = "picread1";

    private static final String METER_NUM_KEY2 = "meternum2";
    private static final String READING_KEY2 = "reading2";
    private static final String PICTURE_READ_KEY2 = "picread2";

    private static final String METER_NUM_KEY3 = "meternum3";
    private static final String READING_KEY3 = "reading3";
    private static final String PICTURE_READ_KEY3 = "picread3";

    private static final String METER_NUM_KEY4 = "meternum4";
    private static final String READING_KEY4 = "reading4";
    private static final String PICTURE_READ_KEY4 = "picread4";

    private static final String METER_NUM_KEY5 = "meternum5";
    private static final String READING_KEY5 = "reading5";
    private static final String PICTURE_READ_KEY5 = "picread5";

    private static final String METER_NUM_KEY6 = "meternum6";
    private static final String READING_KEY6 = "reading6";
    private static final String PICTURE_READ_KEY6 = "picread6";

    private static final String METER_NUM_KEY7 = "meternum7";
    private static final String READING_KEY7 = "reading7";
    private static final String PICTURE_READ_KEY7 = "picread7";

    private static final String METER_NUM_KEY8 = "meternum8";
    private static final String READING_KEY8 = "reading8";
    private static final String PICTURE_READ_KEY8 = "picread8";

    private static final String METER_NUM_KEY9 = "meternum9";
    private static final String READING_KEY9 = "reading9";
    private static final String PICTURE_READ_KEY9 = "picread9";






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
        return sharedPreferences.getString(PHONE_NUMBER_KEY,"000 000 0000");
    }

    static void setIDnumber(String IDnumber){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_NUMBER_KEY,IDnumber);
        editor.apply();
    }

    static String getIDnumber(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PROFILE_SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_NUMBER_KEY,"xxx-xxx-xxx");
    }

    //UtilEr save data // let getters take key as param  //pass position from adapter to takedataacvity
    static void setMeternum(String meternum, String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,meternum);
        editor.apply();
    }

    static String getMeternum(String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    static void setMeterRead(String reading, String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,reading);
        editor.apply();
    }

    static String getMeterRead(String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    static void setImageUri(String uri, String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,uri);
        editor.apply();
    }

    static String getImageUri(String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    static void setTaskId(String taskid){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UTIL_SHARED_NAME,taskid);
        editor.apply();
    }

    static void clearTask(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        editor.apply();
    }

    static String getTaskId(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(UTIL_SHARED_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(UTIL_SHARED_NAME,"none");
    }
}
