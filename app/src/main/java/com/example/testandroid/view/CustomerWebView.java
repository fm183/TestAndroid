package com.example.testandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 自定义webview
 */

public class CustomerWebView extends WebView {
    public CustomerWebView(Context context) {
        super(context);
    }

    public CustomerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
