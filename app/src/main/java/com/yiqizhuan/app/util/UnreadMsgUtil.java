package com.yiqizhuan.app.util;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author LiPeng
 * @create 2024-04-13 5:17 PM
 */
public class UnreadMsgUtil {

    public static void show(TextView numView, int num) {
        if (numView == null) {
            return;
        }
        ViewGroup.LayoutParams lp = numView.getLayoutParams();
        DisplayMetrics dm = numView.getResources().getDisplayMetrics();
        if (num >= 0 && num < 10) {//圆
            lp.width = (int) (15 * dm.density);
            numView.setText(num + "");
        } else if (num > 9 && num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            numView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
            numView.setText(num + "");
        } else {//数字超过两位,显示99+
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            numView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
            numView.setText("99+");
        }
        numView.setLayoutParams(lp);
    }
}