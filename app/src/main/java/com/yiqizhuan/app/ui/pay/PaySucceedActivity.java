package com.yiqizhuan.app.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivityPaySucceedBinding;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.webview.WebActivity;

/**
 * @author LiPeng
 * @create 2024-04-16 7:52 PM
 */
public class PaySucceedActivity extends BaseActivity implements View.OnClickListener {

    ActivityPaySucceedBinding binding;
    private String source;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityPaySucceedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        TextView actionbarRight = binding.includeActionbar.actionbarRight;
        includeActionbar.setText("");
        actionbarRight.setText("确定");
        actionbarRight.setVisibility(View.GONE);
        actionbarBack.setOnClickListener(this);
        actionbarRight.setOnClickListener(this);
        binding.tvBtn.setOnClickListener(this);
        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
            case R.id.actionbar_right:
                if (TextUtils.equals(source, "orderList'")) {
                    goOrder();
                } else {
                    finish();
                }
                break;
            case R.id.tvBtn:
                goOrder();
                break;
        }
    }

    private void goOrder() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ORDER + "?type=0");
        startActivity(intent);
        finish();
    }

}
