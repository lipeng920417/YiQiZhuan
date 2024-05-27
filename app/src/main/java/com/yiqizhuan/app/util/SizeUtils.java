package com.yiqizhuan.app.util;

import android.content.res.Resources;

/**
 * @author LiPeng
 * @create 2024-04-13 5:18 PM
 */
public class SizeUtils {
    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
