package com.figure.anothertest;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class AnotherTest extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
