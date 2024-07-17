package com.yiqizhuan.app.ui.integral;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.GetHistoryExchange;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.databinding.ActivityExchangeProjectBinding;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.integral.item.ExchangeProjectFlexibleItem;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * @author LiPeng
 * @create 2024-05-27 8:03 PM
 * 我的兑换方案
 */
public class ExchangeProjectActivity extends BaseActivity implements View.OnClickListener {
    ActivityExchangeProjectBinding binding;
    private FlexibleAdapter<IFlexible> flexibleAdapter;
    private UserCouponBean queryUserPointsBean;
    private GetHistoryExchange getHistoryExchange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityExchangeProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("积分兑换");
        actionbarBack.setOnClickListener(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            queryUserPointsBean = (UserCouponBean) getIntent().getExtras().getSerializable("queryUserPointsBean");
            getHistoryExchange = (GetHistoryExchange) getIntent().getExtras().getSerializable("getHistoryExchange");
        }
        initView();
        initData();
    }

    private void initView() {
        flexibleAdapter = new FlexibleAdapter<>(null);
        binding.rc.setLayoutManager(new LinearLayoutManager(this));
        binding.rc.setAdapter(flexibleAdapter);
        binding.rc.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        binding.tvName.setText(MMKVHelper.getString("nickName", "")+"用户您好，您可兑换积分额度为");
//        if (getHistoryExchange != null && getHistoryExchange.getData() != null) {
//            binding.tvNum.setText(getHistoryExchange.getData().getTotalContractPoints());
//        }
        if (queryUserPointsBean != null && queryUserPointsBean.getData() != null && !TextUtils.isEmpty(queryUserPointsBean.getData().getTotalUnavailableQuota())) {
            binding.tvNum.setText(queryUserPointsBean.getData().getTotalUnavailableQuota());
        }
        if (getHistoryExchange != null && getHistoryExchange.getData() != null && getHistoryExchange.getData().getPointsInfo() != null && getHistoryExchange.getData().getPointsInfo().size() > 0) {
            for (GetHistoryExchange.PointsInfo pointsInfo : getHistoryExchange.getData().getPointsInfo()) {
                if (pointsInfo.getContractPlan() == 1) {
                    flexibleAdapter.addItem(new ExchangeProjectFlexibleItem(this, pointsInfo, 1));
                }
            }
        }
        flexibleAdapter.addItem(new ExchangeProjectFlexibleItem(this, null, 0));
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
