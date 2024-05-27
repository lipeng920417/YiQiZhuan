package com.yiqizhuan.app.webview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
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
            initView(getIntent().getExtras().getString("url"));
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

    private void initView(String url) {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.lly, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.color_transparent))
                .createAgentWeb().ready().go(url);
        //("file:///android_asset/testh52oc.html");
        mAgentWeb.getWebCreator().getWebView().setBackgroundColor(Color.WHITE);
        mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new AndroidJSInterface(this), "AndroidBridge");
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
