package com.example.testandroid;

import android.app.Application;
import android.util.Log;

import com.example.testandroid.utils.LoggingInterceptors;

public class MainApplication extends Application {

    private static MainApplication instance;

    @Override
    public void onCreate() {
        long startTime = System.currentTimeMillis();
        super.onCreate();
        instance = this;
        Log.d(getClass().getSimpleName(), "onCreate userTime=" + (System.currentTimeMillis() - startTime) + "ms");

        LoggingInterceptors.init();
    }

    public static MainApplication getInstance() {
        return instance;
    }

}
