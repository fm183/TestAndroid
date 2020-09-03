package com.example.testandroid;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.testandroid.dao.AppDataBase;

public class MainApplication extends Application {

    private static MainApplication instance;
    private AppDataBase appDataBase;

    @Override
    public void onCreate() {
        long startTime = System.currentTimeMillis();
        super.onCreate();
        instance = this;
        appDataBase = Room.databaseBuilder(this,AppDataBase.class,"test_database.db").enableMultiInstanceInvalidation().addMigrations(AppDataBase.MIGRATION,AppDataBase.MIGRATION2).build();
        Log.d(getClass().getSimpleName(),"onCreate userTime="+(System.currentTimeMillis() - startTime) + "ms");
    }

    public static MainApplication getInstance(){
        return instance;
    }

    public AppDataBase getAppDataBase(){
        return appDataBase;
    }
}
