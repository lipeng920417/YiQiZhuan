package com.yiqizhuan.app.ui.integral;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.MainActivity;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.QueryUserPointsBean;
import com.yiqizhuan.app.databinding.ActivityIntegralCenterBinding;
import com.yiqizhuan.app.databinding.ActivitySettingBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.setting.SettingActivity;
import com.yiqizhuan.app.util.ToastUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 */
public class IntegralCenterActivity extends BaseActivity implements View.OnClickListener {
    ActivityIntegralCenterBinding binding;
    boolean ivCheckbox;
    private QueryUserPointsBean queryUserPointsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityIntegralCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.actionbarBack.setOnClickListener(this);
        binding.tvMingXi.setOnClickListener(this);
        binding.tvFangAn.setOnClickListener(this);
        binding.ivCheckbox.setOnClickListener(this);
        binding.tvAgreement.setOnClickListener(this);
        binding.tvBtn.setOnClickListener(this);
        queryUserPoints();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()) {

            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tvMingXi:
                startActivity(new Intent(this, IntegralDetailActivity.class));
                break;
            case R.id.tvFangAn:
                startActivity(new Intent(this, ExchangeProjectActivity.class));
                break;
            case R.id.ivCheckbox:
                ivCheckbox = !ivCheckbox;
                if (ivCheckbox) {
                    binding.ivCheckbox.setImageResource(R.mipmap.ic_checkbox_select);
                } else {
                    binding.ivCheckbox.setImageResource(R.mipmap.ic_checkbox);
                }
                break;
            case R.id.tvAgreement:
                startActivity(new Intent(this, IntegralAuthorizationActivity.class));
                break;
            case R.id.tvBtn:
                if (!ivCheckbox) {
                    ToastUtils.showToast("请阅读并勾选 积分兑换授权书");
                    return;
                }
                intent = new Intent(this, IntegralCreateActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("queryUserPointsBean", queryUserPointsBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void queryUserPoints() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.QUERY_USER_POINTS, paramsMap, new BaseCallBack<QueryUserPointsBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, QueryUserPointsBean result) {
                cancelLoading();
                queryUserPointsBean = result;
                if (result != null && result.getData() != null && !TextUtils.isEmpty(result.getData().getTotalPoints())) {
                    binding.tvNum.setText(result.getData().getTotalPoints());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }
}
