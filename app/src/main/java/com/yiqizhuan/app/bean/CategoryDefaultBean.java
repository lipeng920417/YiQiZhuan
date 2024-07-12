package com.yiqizhuan.app.bean;

/**
 * @author LiPeng
 * @create 2024-06-04 10:46 PM
 */
public class CategoryDefaultBean {
    private String id;
    private String name;
    private String parentId;
    private String imageUrl;
    private String topIconUrl;
    private String bannerUrl;
    private int deleted;
    private boolean select;
    private int hide;


    public CategoryDefaultBean() {
    }

    public CategoryDefaultBean(String id, String name, String parentId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.imageUrl = imageUrl;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public String getTopIconUrl() {
        return topIconUrl;
    }

    public void setTopIconUrl(String topIconUrl) {
        this.topIconUrl = topIconUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
