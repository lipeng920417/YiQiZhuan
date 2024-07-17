package com.yiqizhuan.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author LiPeng
 * @create 2024-05-28 2:39 PM
 */
public class GetHistoryExchange extends BaseResult<GetHistoryExchange.Data> {

    public static class Data implements Serializable {
        private String totalContractPoints;
        private List<PointsInfo> pointsInfo;

        public String getTotalContractPoints() {
            return totalContractPoints;
        }

        public void setTotalContractPoints(String totalContractPoints) {
            this.totalContractPoints = totalContractPoints;
        }

        public List<PointsInfo> getPointsInfo() {
            return pointsInfo;
        }

        public void setPointsInfo(List<PointsInfo> pointsInfo) {
            this.pointsInfo = pointsInfo;
        }
    }

    public static class PointsInfo implements Serializable {
        private int contractPlan;//兑换方案 1 全额兑换 2 分期兑换
        private String contractPoints;
        private String signDate;
        private int state;
        private List<PointContractAuditVOs> pointContractAuditVOs;

        public String getContractPoints() {
            return contractPoints;
        }

        public void setContractPoints(String contractPoints) {
            this.contractPoints = contractPoints;
        }

        public String getSignDate() {
            return signDate;
        }

        public void setSignDate(String signDate) {
            this.signDate = signDate;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getContractPlan() {
            return contractPlan;
        }

        public void setContractPlan(int contractPlan) {
            this.contractPlan = contractPlan;
        }

        public List<PointContractAuditVOs> getPointContractAuditVOs() {
            return pointContractAuditVOs;
        }

        public void setPointContractAuditVOs(List<PointContractAuditVOs> pointContractAuditVOs) {
            this.pointContractAuditVOs = pointContractAuditVOs;
        }
    }

    public static class PointContractAuditVOs implements Serializable {
        private String cyclePoints;
        private String exchangeDate;

        public String getCyclePoints() {
            return cyclePoints;
        }

        public void setCyclePoints(String cyclePoints) {
            this.cyclePoints = cyclePoints;
        }

        public String getExchangeDate() {
            return exchangeDate;
        }

        public void setExchangeDate(String exchangeDate) {
            this.exchangeDate = exchangeDate;
        }
    }
}
