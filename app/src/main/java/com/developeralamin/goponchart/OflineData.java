package com.developeralamin.goponchart;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OflineData extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
