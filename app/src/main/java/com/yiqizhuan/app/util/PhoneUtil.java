package com.yiqizhuan.app.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.hjq.permissions.XXPermissions;
import com.yiqizhuan.app.views.dialog.DialogUtil;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-05-27 6:47 PM
 */
public class PhoneUtil {

    public static void getPhone(Context context) {
        final String[] strings = new String[]{Manifest.permission.CALL_PHONE};
        if (!XXPermissions.isGranted(context, strings)) {
            DialogUtil.build2BtnDialog(context, "起点Go商城想要申请您拨打电话权限，用于联系客服?", "确定", "取消", true, new DialogUtil.DialogListener2Btn() {
                @Override
                public void onPositiveClick(View v) {
                    CheckPermission.requestXXPermissions(context, strings, new CheckPermission.PermissionListener() {
                        @Override
                        public void onSuccess(Context context, List<String> data) {
                            callPhone(context);
                        }

                        @Override
                        public void onFailed(Context context, List<String> data) {

                        }

                        /**
                         *  可在此调用，GuidePermission();引导用户前往系统设置页面获取权限
                         * @param context
                         * @param data
                         */
                        @Override
                        public void onNotApply(final Context context, List<String> data) {

                        }
                    });
                }

                @Override
                public void onNegativeClick(View v) {
                }
            }).show();

        } else {
            callPhone(context);
        }
    }

    private static void callPhone(Context context) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + "400-0788-183")));
    }

}
