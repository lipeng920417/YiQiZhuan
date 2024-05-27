package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-04-23 3:57 PM
 */
public class LoginMessage extends BaseResult<LoginMessage.Data> {
    public static class Data {
        String userId;
        String nickName;
        String phone;
        String token;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
