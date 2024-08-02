package com.yiqizhuan.app.ui.order;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.databinding.ActivityMyOrderBinding;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.order.adapter.TabOrderFragmentAdapter;
import com.yiqizhuan.app.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiPeng
 * @create 2024-07-29 10:39 PM
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    ActivityMyOrderBinding binding;
    private List<OrderTabFragment> fragments;
    private List<String> title;
    private TabOrderFragmentAdapter tabOrderFragmentAdapter;
    private OrderTabFragment orderTabFragment;
    int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        ImageButton actionbarBack = binding.includeActionbar.actionbarBack;
        TextView includeActionbar = binding.includeActionbar.actionbarTitle;
        includeActionbar.setText("我的订单");
        actionbarBack.setOnClickListener(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type");
        }
        initTab();
    }

    private void initTab() {
        fragments = new ArrayList<>();
        title = new ArrayList<>();
        title.add("全部");
        title.add("待付款");
        title.add("待收货");
        title.add("已完成");
        title.add("已取消");
        for (String s : title) {
            Bundle bundle = new Bundle();
            if ("全部".equals(s)) {
                bundle.putString("type", "");
            } else if ("待付款".equals(s)) {
                bundle.putString("type", "1");
            } else if ("待收货".equals(s)) {
                bundle.putString("type", "2");
            } else if ("已完成".equals(s)) {
                bundle.putString("type", "3");
            } else if ("已取消".equals(s)) {
                bundle.putString("type", "4");
            }
            orderTabFragment = new OrderTabFragment();
            orderTabFragment.setArguments(bundle);
            fragments.add(orderTabFragment);
        }
        tabOrderFragmentAdapter = new TabOrderFragmentAdapter(getSupportFragmentManager(), fragments, title, this);
        // 设置预加载Fragment个数
        binding.vp.setOffscreenPageLimit(fragments.size());
        binding.vp.setAdapter(tabOrderFragmentAdapter);
        // 设置当前显示标签页为第一页
        binding.vp.setCurrentItem(type);
        // 将ViewPager和TabLayout绑定R
        binding.tab.setupWithViewPager(binding.vp);
        // 设置自定义tab
        for (int i = 0; i < binding.tab.getTabCount(); i++) {
            TabLayout.Tab tabAt = binding.tab.getTabAt(i);
            if (tabAt != null) {
                tabAt.setCustomView(tabOrderFragmentAdapter.getTabView(i));
            }
        }
        // 设置第一页为选中状态
        View view = binding.tab.getTabAt(type).getCustomView();
        setTabStyle(view, true);
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                setTabStyle(view, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                setTabStyle(view, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabStyle(View view, boolean b) {
        if (view == null) {
            return;
        }
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView iv = view.findViewById(R.id.iv);
        RelativeLayout rly = view.findViewById(R.id.rly);
        if (b) {
            tvTitle.setTextColor(getResources().getColor(R.color.color_ff1804));
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvTitle.setTextSize(16f);
            iv.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setTextColor(getResources().getColor(R.color.color_333333));
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvTitle.setTextSize(14f);
            iv.setVisibility(View.GONE);
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
}
