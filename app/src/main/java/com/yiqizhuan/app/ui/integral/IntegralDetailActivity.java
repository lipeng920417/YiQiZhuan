package com.yiqizhuan.app.ui.integral;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.HistoryDetailBean;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.databinding.ActivityIntegralDetailBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.integral.item.IntegralDetailFlexibleItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 * 积分明细
 *
 */
public class IntegralDetailActivity extends BaseActivity implements View.OnClickListener {
    ActivityIntegralDetailBinding binding;
    private FlexibleAdapter<IFlexible> flexibleAdapter;
    private UserCouponBean queryUserPointsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntegralDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("积分明细");
        actionbarBack.setOnClickListener(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            queryUserPointsBean = (UserCouponBean) getIntent().getExtras().getSerializable("queryUserPointsBean");
        }
        initView();
        initData();
        queryUserPoints();
    }

    private void initView() {
        flexibleAdapter = new FlexibleAdapter<>(null);
        binding.rc.setLayoutManager(new LinearLayoutManager(this));
        binding.rc.setAdapter(flexibleAdapter);
        binding.rc.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
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
        }
    }

    private void queryUserPoints() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.HISTORY_DETAIL, paramsMap, new BaseCallBack<BaseResult<List<HistoryDetailBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<HistoryDetailBean>> result) {
                cancelLoading();
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    for (HistoryDetailBean historyDetailBean : result.getData()) {
                        flexibleAdapter.addItem(new IntegralDetailFlexibleItem(IntegralDetailActivity.this, historyDetailBean));
                    }
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}
