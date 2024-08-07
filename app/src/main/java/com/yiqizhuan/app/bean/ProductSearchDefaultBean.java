package com.yiqizhuan.app.bean;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-07-09 7:16 PM
 */
public class ProductSearchDefaultBean {
    private String title;
    private int categoryId;
    private List<ProductListBean.Detail> products;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProductListBean.Detail> getProducts() {
        return products;
    }

    public void setProducts(List<ProductListBean.Detail> products) {
        this.products = products;
    }
}
