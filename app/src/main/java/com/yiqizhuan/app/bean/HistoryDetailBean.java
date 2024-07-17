package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-05-28 10:38 PM
 */
public class HistoryDetailBean {
    private String totalQuotaAction;

    //使用类型 0 退款，1 支付， 2 初始化， 3 合同兑付
    private int type;
    private String typeName;
    private String modifyTime;

    public String getTotalQuotaAction() {
        return totalQuotaAction;
    }

    public void setTotalQuotaAction(String totalQuotaAction) {
        this.totalQuotaAction = totalQuotaAction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
