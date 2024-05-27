package com.yiqizhuan.app.util;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiPeng
 * @create 2024-04-15 4:24 PM
 */
public class UrlUtil {

    /**
     * 获取参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(url)) {
            try {
                Uri uri = Uri.parse(url);
                for (String key : uri.getQueryParameterNames()) {
                    try {
                        params.put(key, uri.getQueryParameter(key));
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }
        return params;
    }

}
