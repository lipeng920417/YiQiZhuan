package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-04-27 9:50 PM
 */
public class CouponInfoBean extends BaseResult<CouponInfoBean.Data>{
    public static class Data {
        private String couponTips;

        public String getCouponTips() {
            return couponTips;
        }

        public void setCouponTips(String couponTips) {
            this.couponTips = couponTips;
        }
    }
}
