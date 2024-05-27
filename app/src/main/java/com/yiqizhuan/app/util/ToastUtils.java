package com.yiqizhuan.app.util;

import android.widget.Toast;

import com.yiqizhuan.app.YQZApp;

/**
 * @author LiPeng
 * @create 2024-04-15 8:59 PM
 */
public class ToastUtils {

    public static void showToast(String message) {
        Toast.makeText(YQZApp.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
