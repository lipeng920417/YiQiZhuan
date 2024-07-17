package com.yiqizhuan.app.ui.integral;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.databinding.ActivityIntegralDetailBinding;
import com.yiqizhuan.app.databinding.ActivitylDetailConfirmBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.webview.WebActivity;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 * 兑换详情确认
 */
public class DetailConfirmActivity extends BaseActivity implements View.OnClickListener {
    ActivitylDetailConfirmBinding binding;
    private UserCouponBean queryUserPointsBean;
    private String totalUnavailableQuota = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitylDetailConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent() != null && getIntent().getExtras() != null) {
            queryUserPointsBean = (UserCouponBean) getIntent().getExtras().getSerializable("queryUserPointsBean");
        }
        initView();
        intiData();
    }

    private void initView() {
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("兑换详情确认");
        actionbarBack.setOnClickListener(this);
        binding.tvALL.setOnClickListener(this);
        binding.tvBtn.setOnClickListener(this);
        binding.edtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    if (Double.parseDouble(editable.toString()) > Double.parseDouble(totalUnavailableQuota)) {
                        binding.tvDuiHuanEDu.setText(totalUnavailableQuota);
                        binding.edtNum.setText(totalUnavailableQuota);
                        binding.edtNum.setSelection(binding.edtNum.getText().length());
                    } else {
                        binding.tvDuiHuanEDu.setText(editable.toString());
                    }
                } else {
                    binding.tvDuiHuanEDu.setText("0");
                }
            }
        });
    }

    private void intiData() {
        if (queryUserPointsBean != null && queryUserPointsBean.getData() != null && !TextUtils.isEmpty(queryUserPointsBean.getData().getTotalUnavailableQuota())) {
            totalUnavailableQuota = queryUserPointsBean.getData().getTotalUnavailableQuota();
            binding.tvMoney.setText("可兑换金额 (元) ：" + queryUserPointsBean.getData().getTotalUnavailableQuota());
        }
        binding.tvName.setText(MMKVHelper.getString("nickName", ""));
        binding.tvIdNumber.setText(MMKVHelper.getString("idNumber", ""));
        binding.tvPhone.setText(MMKVHelper.getString("phone", ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tvALL:
                if (queryUserPointsBean != null && queryUserPointsBean.getData() != null && !TextUtils.isEmpty(queryUserPointsBean.getData().getTotalUnavailableQuota())) {
                    binding.edtNum.setText(queryUserPointsBean.getData().getTotalUnavailableQuota());
                    binding.edtNum.setSelection(binding.edtNum.getText().length());
                }
                break;
            case R.id.tvBtn:
                if (!TextUtils.isEmpty(binding.edtNum.getText().toString()) && Double.parseDouble(binding.edtNum.getText().toString()) > 0) {
                    Intent broker = new Intent(this, WebActivity.class);
                    broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_SIGN_AGREEMENT + "?contractPlan=1" + "&contractPoints=" + binding.edtNum.getText().toString() + "&userName=" + MMKVHelper.getString("userName", ""));
                    startActivity(broker);
                } else {
                    ToastUtils.showToast("请输入积分");
                }
                break;
        }
    }
}
