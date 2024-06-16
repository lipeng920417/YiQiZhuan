package com.yiqizhuan.app.ui.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.MainActivity;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivityLaunchBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.views.dialog.DialogUtil;
import com.yiqizhuan.app.webview.WebActivity;

/**
 * @author LiPeng
 * @create 2024-04-26 9:21 AM
 */
public class LaunchActivity extends BaseActivity {
    ActivityLaunchBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityLaunchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Glide.with(this).asGif().load(R.mipmap.ic_launch).into(binding.iv);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skip();
            }
        }, 2000);
        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip();
            }
        });
    }

    private void skip() {
        String isFirstStart = MMKVHelper.getString("isFirstStart", "0");
        if (TextUtils.equals(isFirstStart, "1")) {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            privacyDialog();
        }
    }

    public void privacyDialog() {
//        我们需要收集您的设备信息等个人信息，
        SpannableString spannableString = new SpannableString("欢迎使用起点Go商城，为了向您提供起点Go商城的软件服务，您可以在相关页面访问、更正、删除您的个人信息并管理您的授权。您可通过阅读《起点Go商城用户服务协议》和《起点Go商城用户隐私协议》了解详细信息，如您同意，请点击“同意”开始接受我们的服务。如您不同意，将退出程序");
        ClickableSpan clickableSpanUserAgreement = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent service = new Intent(LaunchActivity.this, WebActivity.class);
                service.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.SERVICE_AGREEMENT);
                startActivity(service);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.color_ff8e22));
            }
        };
        ClickableSpan clickableSpanSecretAgreement = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent privacy = new Intent(LaunchActivity.this, WebActivity.class);
                privacy.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.PRIVACY_AGREEMENT);
                startActivity(privacy);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.color_ff8e22));
            }
        };
        spannableString.setSpan(clickableSpanUserAgreement, 65, 79, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpanSecretAgreement, 80, 94, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        DialogUtil.build2BtnTitle(LaunchActivity.this, "用户服务隐私协议?", spannableString, "确定", "不同意", false, new DialogUtil.DialogListener2Btn() {
            @Override
            public void onPositiveClick(View v) {
                MMKVHelper.putString("isFirstStart", "1");
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onNegativeClick(View v) {
                finish();
            }
        }).show();
    }

}
