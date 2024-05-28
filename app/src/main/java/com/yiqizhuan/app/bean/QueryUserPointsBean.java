package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-05-27 11:52 PM
 */
public class QueryUserPointsBean extends BaseResult<QueryUserPointsBean.Data> {
    public static class Data {
        private String userId;
        private String totalPoints;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(String totalPoints) {
            this.totalPoints = totalPoints;
        }
    }
}
