package com.yiqizhuan.app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;

/**
 * @author LiPeng
 * @create 2024-04-28 8:41 PM
 */
public class SignatureMD5 {

    public static String getSignatureMD5(Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(

                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            android.content.pm.Signature[] signatures = packageInfo.signatures;


            if (signatures.length > 0) {

                android.content.pm.Signature signature = signatures[0];

                byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("MD5");

                byte[] digest = md.digest(signatureBytes);

  // 将字节数组转换为十六进制字符串

                StringBuilder sb = new StringBuilder();

                for (byte b : digest) {

                    sb.append(String.format("%02x", b));

                }

                return sb.toString();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}
