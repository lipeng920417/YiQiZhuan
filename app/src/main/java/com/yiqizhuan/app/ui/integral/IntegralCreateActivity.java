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
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.databinding.ActivityIntegralCreateBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.ui.base.BaseActivity;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 * 积分兑换
 */
public class IntegralCreateActivity extends BaseActivity implements View.OnClickListener {
    ActivityIntegralCreateBinding binding;
    private UserCouponBean queryUserPointsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityIntegralCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("积分兑换");
        actionbarBack.setOnClickListener(this);
        binding.tvBtn.setOnClickListener(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            queryUserPointsBean = (UserCouponBean) getIntent().getExtras().getSerializable("queryUserPointsBean");
        }
        initData();
    }

    private void initData() {
        binding.tvName.setText(MMKVHelper.getString("nickName", "") + "用户您好，您可兑换积分额度为");
        if (queryUserPointsBean != null && queryUserPointsBean.getData() != null && !TextUtils.isEmpty(queryUserPointsBean.getData().getTotalUnavailableQuota())) {
            binding.tvNum.setText(queryUserPointsBean.getData().getTotalUnavailableQuota());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tvBtn:
                Intent intent;
                Bundle bundle;
                intent = new Intent(this, DetailConfirmActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("queryUserPointsBean", queryUserPointsBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
