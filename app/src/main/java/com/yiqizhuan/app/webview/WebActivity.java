package com.yiqizhuan.app.webview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.just.agentweb.AgentWeb;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivityWebBinding;
import com.yiqizhuan.app.ui.base.BaseActivity;

/**
 * @author LiPeng
 * @create 2024-04-22 3:10 PM
 * <p>
 * http://39.105.56.26/#/index
 */
public class WebActivity extends BaseActivity {
    ActivityWebBinding binding;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            initView(getIntent().getExtras().getString("url"), getIntent().getExtras().getString("data"));
        }

        LiveEventBus.get("webViewClose", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
                    // 返回上一页
                    mAgentWeb.getWebCreator().getWebView().goBack();
                } else {
                    // 无上一页，关闭webview
                    finish();
                }
            }
        });
    }

    private void initView(String url, String data) {

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.lly, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.color_transparent))
                .setWebViewClient(new CustomWebViewClient(data))
                .createAgentWeb()
                .get();
//        mAgentWeb = AgentWeb.with(this)
//                .setAgentWebParent(binding.lly, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//                .useDefaultIndicator(getResources().getColor(R.color.color_transparent))
//                .createAgentWeb().ready().go(url);
        //("file:///android_asset/testh52oc.html");
        mAgentWeb.getWebCreator().getWebView().setBackgroundColor(Color.WHITE);
        mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new AndroidJSInterface(this), "AndroidBridge");

        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        // 启用 DOM Storage
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // 获取当前的 User-Agent 字符串
        String originalUserAgent = webSettings.getUserAgentString();
        // 追加自定义的 User-Agent 信息
        String customUserAgent = originalUserAgent + " isApp=1";
        // 设置新的 User-Agent 字符串
        webSettings.setUserAgentString(customUserAgent);
//        if (!TextUtils.isEmpty(data)) {
//            setSessionStorage("payResult", data);
//        }
        mAgentWeb.getWebCreator().getWebView().loadUrl(url);
    }

    private void setSessionStorage(String key, String value) {
        String jsCode = "sessionStorage.setItem('" + key + "', '" + value + "');";
        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(jsCode, null);
    }


    // 当用户按下返回键时调用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
                mAgentWeb.getWebCreator().getWebView().goBack(); // 返回上一页
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
