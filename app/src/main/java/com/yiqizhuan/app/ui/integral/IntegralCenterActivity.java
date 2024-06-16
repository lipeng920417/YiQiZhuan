package com.yiqizhuan.app.ui.integral;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.GetHistoryExchange;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.databinding.ActivityIntegralCenterBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 * 兑换中心
 */
public class IntegralCenterActivity extends BaseActivity implements View.OnClickListener {
    ActivityIntegralCenterBinding binding;
    boolean ivCheckbox;
    private UserCouponBean queryUserPointsBean;
    private GetHistoryExchange getHistoryExchange;

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
        binding.ivHint.setOnClickListener(this);
        queryUserPoints();
        getHistoryExchange();
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
                intent = new Intent(this, IntegralDetailActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("queryUserPointsBean", queryUserPointsBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tvFangAn:
                intent = new Intent(this, ExchangeProjectActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("queryUserPointsBean", queryUserPointsBean);
                bundle.putSerializable("getHistoryExchange", getHistoryExchange);
                intent.putExtras(bundle);
                startActivity(intent);
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
            case R.id.ivHint:
                DialogUtil.build1BtnDialog(this, "当前积分为还未兑换积分数，兑换已发放可消费积分可以去我的页面查看", "我知道了", true, new DialogUtil.DialogListener1Btn() {
                    @Override
                    public void onPositiveClick(View v) {

                    }
                }).show();
                break;
        }
    }

    private void queryUserPoints() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.QUERY_USER_COUPON, paramsMap, new BaseCallBack<UserCouponBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, UserCouponBean result) {
                cancelLoading();
                queryUserPointsBean = result;
                if (result != null && result.getData() != null && !TextUtils.isEmpty(result.getData().getTotalUnavailableQuota())) {
                    binding.tvNum.setText(result.getData().getTotalUnavailableQuota());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    private void getHistoryExchange() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.GET_HISTORY_EXCHANGE, paramsMap, new BaseCallBack<GetHistoryExchange>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, GetHistoryExchange result) {
                getHistoryExchange = result;
                if (result != null && result.getData() != null && result.getData().getPointsInfo() != null && result.getData().getPointsInfo().size() > 0) {
                    binding.tvFangAn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }
}
