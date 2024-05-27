package com.yiqizhuan.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.ui.home.adapter.Common1BannerAdapter;
import com.yiqizhuan.app.ui.home.adapter.Common4BannerAdapter;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;

/**
 * @author LiPeng
 * @create 2023-11-27 2:13 PM
 * 通用Banner1
 */
public class Common1Banner extends LinearLayout {
    private Banner banner;

    public Common1Banner(Context context) {
        super(context);
        initView(context);
    }

    public Common1Banner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Common1Banner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.common_banner1, this);
        banner = findViewById(R.id.banner);
    }

    public void initData(List<ProductListBean.Detail> detail, Context context, String type) {
        banner.setAdapter(new Common1BannerAdapter(context, detail, type)).setIndicator(new CircleIndicator(getContext()));
    }
}
