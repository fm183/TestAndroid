package com.example.testandroid;

import android.app.Application;
import android.util.Log;

import com.example.testandroid.utils.LoggingInterceptors;
import com.network.lib.request.NetConfig;
import com.network.lib.request.RequestManager;

import okhttp3.Interceptor;

public class MainApplication extends Application {

    private static MainApplication instance;

    @Override
    public void onCreate() {
        long startTime = System.currentTimeMillis();
        super.onCreate();
        instance = this;
        Log.d(getClass().getSimpleName(), "onCreate userTime=" + (System.currentTimeMillis() - startTime) + "ms");

        LoggingInterceptors.init();

        RequestManager.registerConfig(new NetConfig() {
            @Override
            public String configBaseUrl() {
                return "https://app.apad.pro/";
            }

            @Override
            public Interceptor[] configInterceptors() {
                return new Interceptor[]{};
            }

            @Override
            public long configConnectTimeoutMills() {
                return 10 * 1000;
            }

            @Override
            public long configReadTimeoutMills() {
                return 10 * 1000;
            }

            @Override
            public boolean configLogEnable() {
                return BuildConfig.DEBUG;
            }
        });
    }

    public static MainApplication getInstance() {
        return instance;
    }

}
