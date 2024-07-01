package com.yiqizhuan.app.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.db.MMKVHelper;
import com.yiqizhuan.app.ui.detail.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiPeng
 * @create 2024-06-24 11:30 AM
 */
public class SkipActivityUtil {

    public static void goGoodsDetail(Context context, String productId, String type) {
        if (TextUtils.isEmpty(MMKVHelper.getString("token", ""))) {
            LiveEventBus.get("goToLogin").post("");
        } else {
            Intent intent = new Intent(context, GoodsDetailActivity.class);
            intent.putExtra("productId", productId);
            intent.putExtra("type", type);
            context.startActivity(intent);
        }
    }

    public static void goGoodsDetail(Context context, String productId, String type, String goodsId) {
        if (TextUtils.isEmpty(MMKVHelper.getString("token", ""))) {
            LiveEventBus.get("goToLogin").post("");
        } else {
            Intent intent = new Intent(context, GoodsDetailActivity.class);
            intent.putExtra("productId", productId);
            intent.putExtra("type", type);
            intent.putExtra("goodsId", goodsId);
            context.startActivity(intent);
        }
    }
}
