package com.yiqizhuan.app.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.List;

/**
 * @Author : lipeng
 * @Time : 2023/3/23
 * @Description : 权限管理类
 */
public class CheckPermission {
    private static final String TAG = "CheckPermission";

    /**
     * 获取权限和权限三个状态回调
     *
     * @param context
     * @param permission
     * @param listener
     */
    public static void requestXXPermissions(Context context, String[] permission, final PermissionListener listener) {
        XXPermissions.with(context)
                // 申请单个权限
//                .permission(permission)
                // 申请多个权限
                .permission(permission)
                // 设置权限请求拦截器（局部设置）
//                .interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
//                .unchecked()
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            Log.d(TAG, "获取部分权限成功，但部分权限未正常授予");
                            return;
                        }
                        listener.onSuccess(context, permissions);
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            Log.d(TAG, "被永久拒绝授权，请手动授予录音和日历权限");
//                            如果是被永久拒绝就跳转到应用权限系统设置页面
//                            XXPermissions.startPermissionActivity(context, permissions);
                            listener.onNotApply(context, permissions);
                        } else {
                            listener.onFailed(context, permissions);
                        }
                    }
                });
    }

    /**
     * 判断一个或多个权限是否全部授予了
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean isGranted(Context context, String[] permission) {
        return XXPermissions.isGranted(context, permission);
    }

    /**
     * 获取没有授予的权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static List<String> getDenied(Context context, String[] permission) {
        return XXPermissions.getDenied(context, permission);
    }

    /**
     * 判断一个或多个权限是否被永久拒绝了（一定要在权限申请的回调方法中调用才有效果）
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean isPermanentDenied(Activity activity, String[] permission) {
        return XXPermissions.isPermanentDenied(activity, permission);
    }

    /**
     * 跳转到应用权限设置页
     *
     * @param context
     * @param permissions
     */
    public static void startPermissionActivity(Context context, List<String> permissions) {
        XXPermissions.startPermissionActivity(context, permissions);
    }

    public interface PermissionListener {
        void onSuccess(Context context, List<String> data);

        void onFailed(Context context, List<String> data);

        void onNotApply(Context context, List<String> data);
    }

}