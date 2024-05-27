package com.yiqizhuan.app.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yiqizhuan.app.YQZApp;

/**
 * @author LiPeng
 * @create 2024-04-19 2:43 PM
 */
public class AppUtil {

    public static String getVersionName() {
        String versionName = null;
        PackageManager manager = YQZApp.getContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(YQZApp.getContext().getPackageName(), 0);
            versionName = info.versionName;
            int versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        PackageManager manager = YQZApp.getContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(YQZApp.getContext().getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
