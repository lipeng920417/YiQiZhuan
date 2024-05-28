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
        private List<PointContractAuditVOs> pointContractAuditVOs;

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
