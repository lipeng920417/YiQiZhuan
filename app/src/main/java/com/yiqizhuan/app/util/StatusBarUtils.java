package com.yiqizhuan.app.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author LiPeng
 * @create 2024-04-26 10:31 AM
 */
public class StatusBarUtils {

    /**
     * 设状态栏高度
     */
    public static void setViewHeaderPlaceholder(View viewHeaderPlaceholder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHeaderPlaceholder.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = viewHeaderPlaceholder.getLayoutParams();
            int statusBarHeight = getStatusBarHeight(viewHeaderPlaceholder.getContext());
            layoutParams.height = statusBarHeight;
            viewHeaderPlaceholder.setLayoutParams(layoutParams);
        } else {
            viewHeaderPlaceholder.setVisibility(View.GONE);
        }
    }

     /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}
