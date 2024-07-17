package com.yiqizhuan.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author LiPeng
 * @create 2024-06-16 12:03 PM
 */
public class BigDecimalUtil {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
