package com.yiqizhuan.app.util;

import android.os.SystemClock;

/**
 * 处理重复点击事件
 */
public class ClickUtil {
    public static long sLastClickTime;
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static final int MIN_CLICK_DELAY_SHORT_TIME = 500;

    private static long lastClickTime;

    public static boolean isRealClick() {
        if (SystemClock.elapsedRealtime() - sLastClickTime < 400) {
            return false;
        }
        sLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isShortFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_SHORT_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
