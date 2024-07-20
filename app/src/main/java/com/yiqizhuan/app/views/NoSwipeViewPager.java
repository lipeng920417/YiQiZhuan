package com.yiqizhuan.app.views;

/**
 * @author LiPeng
 * @create 2024-07-17 2:01 PM
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NoSwipeViewPager extends ViewPager {

    public NoSwipeViewPager(Context context) {
        super(context);
    }

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 不拦截触摸事件
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 不处理触摸事件
        return false;
    }
}
