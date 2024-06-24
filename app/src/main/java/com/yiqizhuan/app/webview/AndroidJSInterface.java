package com.yiqizhuan.app.webview;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.db.MMKVHelper;

import org.json.JSONObject;

/**
 * @author LiPeng
 * @create 2024-04-19 4:55 PM
 */
public class AndroidJSInterface {
    Context context;

    public AndroidJSInterface(Context context) {
        this.context = context;
    }


    /**
     * 关闭WebActivity
     */
    @JavascriptInterface
    public void webViewClose(String data) {
        LiveEventBus.get("webViewClose").post("");
    }

    /**
     * 获取App的token
     */
    @JavascriptInterface
    public String getAppToken(String data) {
        return MMKVHelper.getString("token", "");
    }

    /**
     * token 失效告知App
     */
    @JavascriptInterface
    public void expiredToken(String data) {
        LiveEventBus.get("expiredToken").post("");
    }

    /**
     * 去登陆
     */
    @JavascriptInterface
    public void goToLogin(String data) {
        LiveEventBus.get("goToLogin").post("");
    }


    /**
     * 购物车点击跳转商品详情
     */
    @JavascriptInterface
    public void jumpToDetail(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        LiveEventBus.get("jumpToDetail").post(data);
    }

    /**
     * 购物车数量
     */
    @JavascriptInterface
    public void changeCartNum(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            String cartNum = jsonObject.getString("cartNum");
            LiveEventBus.get("changeCartNum").post(cartNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 去购物车
     */
    @JavascriptInterface
    public void jumpShopCart(String data) {
        LiveEventBus.get("webViewClose").post("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LiveEventBus.get("shopping").post("");
            }
        }, 100);
    }

    @JavascriptInterface
    public void goHome(String data) {
        LiveEventBus.get("webViewClose").post("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LiveEventBus.get("goHome").post("");
            }
        }, 100);
    }

    @JavascriptInterface
    public void getAddressFormJs(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        LiveEventBus.get("getAddressFormJs").post(data);
    }

}
