package com.yiqizhuan.app.bean;

import java.io.Serializable;

/**
 * @author LiPeng
 * @create 2024-06-04 10:46 PM
 */
public class GetDiscountCategoryBean extends BaseResult<GetDiscountCategoryBean.Data> {

    public static class Data implements Serializable {
        private String discountUrl;
        private String categoryId;

        public String getDiscountUrl() {
            return discountUrl;
        }

        public void setDiscountUrl(String discountUrl) {
            this.discountUrl = discountUrl;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}
