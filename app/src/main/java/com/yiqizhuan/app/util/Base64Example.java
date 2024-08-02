package com.yiqizhuan.app.util;

/**
 * @author LiPeng
 * @create 2024-07-29 11:22 AM
 */

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Base64Example {


    // Base64 编码
    public static String base64Encode(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    // Base64 解码
    public static String base64Decode(String input) {
        byte[] decodedBytes = Base64.decode(input, Base64.NO_WRAP);
        return new String(decodedBytes);
    }

    // Base64 编码
    public static String base64EncodeUtf8(String input) {
        byte[] data = new byte[0];
        try {
            data = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String string = Base64.encodeToString(data, Base64.NO_WRAP);
        Log.d("Base64.encodeToString", string);
        return string;
    }

    // Base64 解码
    public static String base64DecodeUtf8(String input) {
        byte[] decodedBytes = Base64.decode(input, Base64.NO_WRAP);
        String string = "";
        try {
            string = new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return string;
    }

}
