package com.example.testandroid;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testandroid.constant.Constant;
import com.example.testandroid.utils.FileUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        post();
        uploadFile();
    }

    private void getSync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Builder().url(Constant.LIST_ALL_STUDENT).get().build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    Log.d(TAG, "getSync run: isSuccess=" + response.isSuccessful() + ",code=" + response.code() + ",body" + response.body());
                    ResponseBody body = response.body();
                    if (body == null) {
                        return;
                    }
                    Log.d(TAG, "getSync run: body=" + body.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void getASync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Builder().url(Constant.LIST_ALL_STUDENT).get().build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.d(TAG, "getASync run: isSuccess=" + response.isSuccessful() + ",code=" + response.code() + ",body" + response.body() + ",isMainThread=" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                            ResponseBody body = response.body();
                            if (body == null) {
                                return;
                            }
                            Log.d(TAG, "getASync run: body=" + body.string());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void post() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("json", "json");
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Builder().url(Constant.LIST_ALL_STUDENT).post(requestBody).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.d(TAG, "post run: isSuccess=" + response.isSuccessful() + ",code=" + response.code() + ",body" + response.body() + ",isMainThread=" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                            ResponseBody body = response.body();
                            if (body == null) {
                                return;
                            }
                            Log.d(TAG, "post run: body=" + body.string());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void uploadFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i=0;i < 1000;i ++){
                        stringBuilder.append("Hello world Hello world Hello world Hello world Hello world ").append("\n");
                    }
                    File file = FileUtils.save(stringBuilder.toString());
                    if(file == null){
                        return;
                    }
                    OkHttpClient client = new OkHttpClient();
                    MultipartBody body = new MultipartBody.Builder()
                            .addFormDataPart("file", file.getName(),createCustomRequestBody(MediaType.get("application/octet-stream"),file))
                            .build();
                    Request request = new Builder()
                            .url(Constant.UPLOAD_FILE)
                            .post(body)
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.d(TAG, "uploadFile run: isSuccess=" + response.isSuccessful() + ",code=" + response.code() + ",body" + response.body() + ",isMainThread=" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                            ResponseBody body = response.body();
                            if (body == null) {
                                return;
                            }
                            Log.d(TAG, "uploadFile run: body=" + body.string());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(@NotNull BufferedSink sink) {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long totalCount = contentLength();
                    long downloadCount = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        downloadCount += readCount;
                        float progress = downloadCount * 1f / totalCount;
                        Log.d(TAG, "writeTo: totalBytes=" + totalCount+",remaining="+downloadCount+",isDone="+(downloadCount == totalCount)+",progress="+progress);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
