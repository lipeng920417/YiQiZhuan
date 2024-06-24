package com.yiqizhuan.app.webview;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.WebView;

import com.just.agentweb.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    String data;
    public CustomWebViewClient(String data) {
        this.data =data;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!TextUtils.isEmpty(data)){
            String jsCode = "sessionStorage.setItem('payResult', '" + data + "');";
            view.evaluateJavascript(jsCode, null);
        }
    }


}
