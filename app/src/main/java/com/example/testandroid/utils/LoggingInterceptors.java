package com.example.testandroid.utils;

import com.example.testandroid.MainApplication;
import com.example.testandroid.R;
import com.example.testandroid.constant.Constant;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class LoggingInterceptors {
    private static final Logger logger = Logger.getLogger(LoggingInterceptors.class.getName());
    private final OkHttpClient client;
    private final Retrofit retrofit;
    private static LoggingInterceptors instance;

    public synchronized static LoggingInterceptors getInstance(){
        if (instance == null) {
            instance = new LoggingInterceptors();
        }
        return instance;
    }

    public static void init(){
        instance = new LoggingInterceptors();
    }

    public OkHttpClient getClient(){
        return client;
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public LoggingInterceptors() {
        logger.info("LoggingInterceptors");
        client = new OkHttpClient.Builder().addInterceptor((chain -> {
            long t1 = System.nanoTime();
            Request request = chain.request();
            logger.info(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            logger.info(String.format(MainApplication.getInstance().getString(R.string.response_text),
                    request.url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        })).build();
        retrofit  = new Retrofit.Builder().baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}