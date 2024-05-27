package com.yiqizhuan.app.bean;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-04-26 2:14 PM
 */
public class ProductDefaultBean extends BaseResult<ProductDefaultBean.Data>{

    public static class Data {
        //精选汇
       private List<ProductListBean.Detail> curated_product;
        //共享汇
        private List<ProductListBean.Detail> earn_together;
        //每日
        private List<ProductListBean.Detail> daily_exchange;
        //畅享
        private List<ProductListBean.Detail> enjoyable_exchange;

        public List<ProductListBean.Detail> getCurated_product() {
            return curated_product;
        }

        public void setCurated_product(List<ProductListBean.Detail> curated_product) {
            this.curated_product = curated_product;
        }

        public List<ProductListBean.Detail> getEarn_together() {
            return earn_together;
        }

        public void setEarn_together(List<ProductListBean.Detail> earn_together) {
            this.earn_together = earn_together;
        }

        public List<ProductListBean.Detail> getDaily_exchange() {
            return daily_exchange;
        }

        public void setDaily_exchange(List<ProductListBean.Detail> daily_exchange) {
            this.daily_exchange = daily_exchange;
        }

        public List<ProductListBean.Detail> getEnjoyable_exchange() {
            return enjoyable_exchange;
        }

        public void setEnjoyable_exchange(List<ProductListBean.Detail> enjoyable_exchange) {
            this.enjoyable_exchange = enjoyable_exchange;
        }
    }
}
