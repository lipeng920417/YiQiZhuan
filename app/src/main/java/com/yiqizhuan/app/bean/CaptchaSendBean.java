package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-04-22 8:40 PM
 */
public class CaptchaSendBean extends BaseResult<CaptchaSendBean.Data> {

    public static class Data {
        String phone;
        String code;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
