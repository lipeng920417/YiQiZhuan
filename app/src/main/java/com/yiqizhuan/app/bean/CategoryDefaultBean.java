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

    public CategoryDefaultBean(String id, String name, String parentId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.imageUrl = imageUrl;
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
