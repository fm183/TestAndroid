package com.example.testandroid.manager;

import android.os.Looper;
import android.util.Log;

import com.example.testandroid.constant.Constant;
import com.example.testandroid.utils.FileUtils;
import com.example.testandroid.utils.LoggingInterceptors;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class OKHttpManager {

    private static final String TAG = OKHttpManager.class.getSimpleName();


    public void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String downloadUrl = Constant.DOWNLOAD_FILE_URL;
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
                Log.d(TAG, "download: fileName=" + fileName + ",downloadUrl=" + downloadUrl);
                File file = FileUtils.getDownloadFile(fileName);
                if (file == null) {
                    Log.d(TAG, "download: fail, dir is null");
                    return;
                }

                long downloadLength = 0;
                if (file.exists()) {
                    downloadLength = file.length();
                }
                long totalLength = getContentLength(downloadUrl);
                if (totalLength == 0) {
                    Log.d(TAG, "download: url is error");
                    return;
                }
                if (downloadLength == totalLength) {
                    Log.d(TAG, "download: done");
                    return;
                }

                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + downloadLength + "-" + totalLength)  //断点续传要用到的，指示下载的区间
                        .url(downloadUrl).build();
                try {
                    Response response = LoggingInterceptors.getInstance().getClient().newCall(request).execute();
                    ResponseBody body = response.body();
                    if (body == null) {
                        Log.d(TAG, "download: body is null");
                        return;
                    }
                    InputStream inputStream = body.byteStream();
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(downloadLength);
                    byte[] b = new byte[1024];
                    int total = 0;
                    int len;
                    while ((len = inputStream.read(b)) != -1) {
                        total += len;
                        randomAccessFile.write(b, 0, len);
                        //计算已经下载的百分比
                        int progress = (int) ((total + downloadLength) * 100 / totalLength);
                        Log.d(TAG, "download: progress=" + progress + ",total="+total+",downloadLength="+downloadLength+",totalLength="+totalLength);
                    }
                    body.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 得到下载内容的完整大小
     *
     * @param downloadUrl 下载地址
     * @return 下载总大小
     */
    private long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                long contentLength = body == null ? 0 : body.contentLength();
                if (body != null) {
                    body.close();
                }
                return contentLength;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void getSync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(Constant.LIST_ALL_STUDENT).get().build();
                    Call call = LoggingInterceptors.getInstance().getClient().newCall(request);
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

    public void getASync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(Constant.LIST_ALL_STUDENT).get().build();
                    Call call = LoggingInterceptors.getInstance().getClient().newCall(request);
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

    public void post() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("json", "json");
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(Constant.LIST_ALL_STUDENT).post(requestBody).build();
                    Call call = LoggingInterceptors.getInstance().getClient().newCall(request);
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
                    for (int i = 0; i < 1000; i++) {
                        stringBuilder.append("Hello world Hello world Hello world Hello world Hello world ").append("\n");
                    }
                    File file = FileUtils.save(stringBuilder.toString());
                    if (file == null) {
                        return;
                    }
                    MultipartBody body = new MultipartBody.Builder()
                            .addFormDataPart("file", file.getName(), createCustomRequestBody(MediaType.get("application/octet-stream"), file))
                            .build();
                    Request request = new Request.Builder()
                            .url(Constant.UPLOAD_FILE)
                            .post(body)
                            .build();
                    Call call = LoggingInterceptors.getInstance().getClient().newCall(request);
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

    public RequestBody createCustomRequestBody(final MediaType contentType, final File file) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(@NotNull BufferedSink sink) {
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
                        Log.d(TAG, "writeTo: totalBytes=" + totalCount + ",remaining=" + downloadCount + ",isDone=" + (downloadCount == totalCount) + ",progress=" + progress);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
