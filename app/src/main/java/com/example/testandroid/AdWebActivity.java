package com.example.testandroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.network.lib.intf.DownloadFileListener;
import com.network.lib.request.DownloadFileManager;

import java.io.File;
import java.util.List;

public class AdWebActivity extends BaseActivity {

    private static final String TAG = AdWebActivity.class.getSimpleName();

    public static void launch(Context context, String loadUrl, String title){
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context,AdWebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("loadUrl",loadUrl);
        context.startActivity(intent);
    }


    private WebView webView;
    private ProgressBar progressBar;
    private MyToolBar myToolBar;
    private String loadUrl;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_adweb;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        loadUrl = intent.getStringExtra("loadUrl");
        String title = intent.getStringExtra("title");
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
        myToolBar = findViewById(R.id.my_tool_bar);
        myToolBar.setTitle(title);

        initWebView();
        DownloadFileManager.getInstance().addListener(new DownloadFileListener() {
            @Override
            public void onDownloadStart(String url, long totalLength) {
                Log.d(TAG, "onDownloadStart: url="+url+",totalLength="+totalLength);
            }

            @Override
            public void onDownloadProgress(String url, long totalLength, long downloadLength, int progress) {
                Log.d(TAG, "onDownloadProgress: url="+url+",totalLength="+totalLength+",downloadLength="+downloadLength+",progress="+progress);

            }

            @Override
            public void onDownloadFinish(String url, File file) {
                Log.d(TAG, "onDownloadFinish: url="+url);
                installApk(file);
            }

            @Override
            public void onDownloadError(String url, String error) {
                Log.d(TAG, "onDownloadError: url="+url+",error="+error);
            }
        });
    }

    private void installApk(File file){
        if (file == null || !file.isFile() || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            //与manifest中定义的provider中的authorities="cn.wlantv.kznk.fileprovider"保持一致
            Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".gdt.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    public void initWebView() {
        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                myToolBar.post(() -> myToolBar.setTitle(title));
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isDeepLink(url)){
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (deviceCanHandleIntent(getApplicationContext(), intent)){
                        try{
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(view.getContext(),"应用跳转被拒绝",Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }else {
                    return false;
                }
         }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            try {
                httpDownload(url);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        webView.loadUrl(this.loadUrl);
    }

    /**
     * 自制的下载
     */
    private void httpDownload(String url) {
        Log.d(TAG, "httpDownload: url="+url);
        DownloadFileManager.getInstance().download(url,createDownloadPath(),getFileNameByUrl(url));
    }
    private String createDownloadPath(){
        try {
            File dirFile = MainApplication.getInstance().getFilesDir();
            if (!dirFile.exists() || !dirFile.isDirectory()) {
                return "";
            }
            File dir = new File(dirFile,"download");
            if (!dir.exists()) {
                boolean success = dir.mkdirs();
                if(!success){
                    return "";
                }

            }
            return dir.getPath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String getFileNameByUrl(String url){
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        try {
            int lastIndexOf = url.lastIndexOf("/");
            if (lastIndexOf < 0) {
                return "";
            }
            return url.substring(lastIndexOf + 1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    //加载落地页的 webview 中
    public static boolean isDeepLink(final String url) {
        return !isHttpUrl(url);
    }
    public static boolean deviceCanHandleIntent(final Context context, final Intent intent) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            return !activities.isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }
    public static boolean isHttpUrl(final String url) {
        if (url == null) {
            return false;
        }
        return url.startsWith("http:") || url.startsWith("https:");
    }
}
