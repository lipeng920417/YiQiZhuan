package com.yiqizhuan.app.db;

import com.tencent.mmkv.MMKV;
import com.yiqizhuan.app.bean.LoginMessage;
import com.yiqizhuan.app.bean.UserInfoBean;

/**
 * @author LiPeng
 * @create 2024-04-23 4:30 PM
 */
public class MMKVHelper {
    private static final String MMKV_ID = "mmkv_id";

    public static void putString(String key, String value) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(key, value);
    }

    public static String getString(String key, String defaultValue) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(key, defaultValue);
    }

    public static void remove(String key) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.removeValueForKey(key);
    }

    public static void clearAll() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.clearAll();
    }

    public static void putLoginMessage(LoginMessage loginMessage) {
        if (loginMessage.getData() != null) {
            MMKVHelper.putString("userId", loginMessage.getData().getUserId());
            MMKVHelper.putString("nickName", loginMessage.getData().getNickName());
            MMKVHelper.putString("phone", loginMessage.getData().getPhone());
            MMKVHelper.putString("token", loginMessage.getData().getToken());
        }
    }

    public static void putUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean.getData() != null) {
            MMKVHelper.putString("userId", userInfoBean.getData().getId());
            MMKVHelper.putString("nickName", userInfoBean.getData().getNickName());
            MMKVHelper.putString("phone", userInfoBean.getData().getPhoneNumber());
            MMKVHelper.putString("avatarUrl", userInfoBean.getData().getAvatarUrl());
            MMKVHelper.putString("state", userInfoBean.getData().getState());
        }
    }

    public static void removeLoginMessage() {
        MMKVHelper.remove("userId");
        MMKVHelper.remove("nickName");
        MMKVHelper.remove("phone");
        MMKVHelper.remove("avatarUrl");
        MMKVHelper.remove("state");
        MMKVHelper.remove("token");
    }


}