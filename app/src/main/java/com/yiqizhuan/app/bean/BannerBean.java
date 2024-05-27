package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-04-24 11:24 PM
 */
public class BannerBean {
    private String mainPhotoUrl;
    private String productId;

    private String mainPhotoUrl1;
    private String productId1;

    public BannerBean(String mainPhotoUrl, String productId, String mainPhotoUrl1, String productId1) {
        this.mainPhotoUrl = mainPhotoUrl;
        this.productId = productId;
        this.mainPhotoUrl1 = mainPhotoUrl1;
        this.productId1 = productId1;
    }

    public String getMainPhotoUrl() {
        return mainPhotoUrl;
    }

    public void setMainPhotoUrl(String mainPhotoUrl) {
        this.mainPhotoUrl = mainPhotoUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMainPhotoUrl1() {
        return mainPhotoUrl1;
    }

    public void setMainPhotoUrl1(String mainPhotoUrl1) {
        this.mainPhotoUrl1 = mainPhotoUrl1;
    }

    public String getProductId1() {
        return productId1;
    }

    public void setProductId1(String productId1) {
        this.productId1 = productId1;
    }
}
