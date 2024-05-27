package com.yiqizhuan.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BannerBean;
import com.yiqizhuan.app.ui.home.adapter.CommonBannerAdapter;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;

/**
 * @author LiPeng
 * @create 2023-11-27 2:13 PM
 * 通用Banner
 */
public class CommonBanner extends LinearLayout {
    private Banner banner;

    public CommonBanner(Context context) {
        super(context);
        initView(context);
    }

    public CommonBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.common_banner, this);
        banner = findViewById(R.id.banner);
    }

    public void initData(List<BannerBean> picUrls, Context context) {
        banner.setAdapter(new CommonBannerAdapter(context, picUrls)).setIndicator(new CircleIndicator(getContext()));
    }
}
