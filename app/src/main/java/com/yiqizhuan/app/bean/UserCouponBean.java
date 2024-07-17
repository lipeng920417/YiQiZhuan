package com.yiqizhuan.app.bean;

import java.io.Serializable;

/**
 * @author LiPeng
 * @create 2024-04-25 2:11 PM
 */
public class UserCouponBean extends BaseResult<UserCouponBean.Data>{
    public static class Data implements Serializable {
        //用户ID
        private String userId;
        //总额度
        private String totalQuota;
        //每月换购额度
        private String monthQuota;
        //剩余每月换购额度
        private String remainMonthQuota;
        private String lastMonthQuota;
        //共创销售额（暂时不用）
        private String salesQuota;
        //商券变现（暂时不用）
        private String couponMonetization;
        //佣金（暂时不用）
        private String commission;
        //生效日期
        private String validTime;
        //生效标识 0 生效 1失效
        private String validFlag;
        private String totalUnavailableQuota;


        public String getTotalUnavailableQuota() {
            return totalUnavailableQuota;
        }

        public void setTotalUnavailableQuota(String totalUnavailableQuota) {
            this.totalUnavailableQuota = totalUnavailableQuota;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTotalQuota() {
            return totalQuota;
        }

        public void setTotalQuota(String totalQuota) {
            this.totalQuota = totalQuota;
        }

        public String getMonthQuota() {
            return monthQuota;
        }

        public void setMonthQuota(String monthQuota) {
            this.monthQuota = monthQuota;
        }

        public String getRemainMonthQuota() {
            return remainMonthQuota;
        }

        public void setRemainMonthQuota(String remainMonthQuota) {
            this.remainMonthQuota = remainMonthQuota;
        }

        public String getLastMonthQuota() {
            return lastMonthQuota;
        }

        public void setLastMonthQuota(String lastMonthQuota) {
            this.lastMonthQuota = lastMonthQuota;
        }

        public String getSalesQuota() {
            return salesQuota;
        }

        public void setSalesQuota(String salesQuota) {
            this.salesQuota = salesQuota;
        }

        public String getCouponMonetization() {
            return couponMonetization;
        }

        public void setCouponMonetization(String couponMonetization) {
            this.couponMonetization = couponMonetization;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }

        public String getValidFlag() {
            return validFlag;
        }

        public void setValidFlag(String validFlag) {
            this.validFlag = validFlag;
        }
    }
}
