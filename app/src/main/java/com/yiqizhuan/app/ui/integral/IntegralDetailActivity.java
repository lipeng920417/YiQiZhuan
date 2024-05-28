package com.yiqizhuan.app.ui.integral;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.QueryUserPointsBean;
import com.yiqizhuan.app.databinding.ActivityIntegralCenterBinding;
import com.yiqizhuan.app.databinding.ActivityIntegralCreateBinding;
import com.yiqizhuan.app.databinding.ActivityIntegralDetailBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.ToastUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 */
public class IntegralDetailActivity extends BaseActivity implements View.OnClickListener {
    ActivityIntegralDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntegralDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("积分明细");
        actionbarBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;

        }
    }
}
