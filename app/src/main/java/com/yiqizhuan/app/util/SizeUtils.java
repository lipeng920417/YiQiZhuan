package com.yiqizhuan.app.util;

import android.content.Context;
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

    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) dpValue;
    }

}
