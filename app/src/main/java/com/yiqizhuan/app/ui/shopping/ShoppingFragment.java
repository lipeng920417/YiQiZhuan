package com.yiqizhuan.app.ui.shopping;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.just.agentweb.AgentWeb;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.FragmentShoppingBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.webview.AndroidJSInterface;

public class ShoppingFragment extends BaseFragment {

    private FragmentShoppingBinding binding;
    private AgentWeb mAgentWeb;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        initView();
        return root;
    }

    private void initView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.lly, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.color_transparent))
                .createAgentWeb().ready().go(BuildConfig.BASE_WEB_URL + WebApi.WEB_CART);
        mAgentWeb.getWebCreator().getWebView().setBackgroundColor(Color.WHITE);
        mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new AndroidJSInterface(getContext()), "AndroidBridge");
        LiveEventBus.get("webViewClose", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
                    // 返回上一页
                    mAgentWeb.getWebCreator().getWebView().goBack();
                } else {
                    // 无上一页，关闭webview
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (TextUtils.equals(MMKVHelper.getString("webView", "webView"), "reload")) {
                mAgentWeb.getWebCreator().getWebView().loadUrl(BuildConfig.BASE_WEB_URL + WebApi.WEB_CART);
                //                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAgentWeb.getWebCreator().getWebView().reload();
//                    }
//                }, 300);
            } else {
                mAgentWeb.getWebCreator().getWebView().reload();
                MMKVHelper.putString("webView", "reload");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}