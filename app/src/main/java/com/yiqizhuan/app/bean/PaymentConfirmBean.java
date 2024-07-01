package com.yiqizhuan.app.bean;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-06-29 12:48 AM
 */
public class PaymentConfirmBean {

    private String totalUseCoupon;
    private String remainTotalQuota;
    private String remainMonthQuota;
    private String totalUseMonthCoupon;
    private String totalPrice;
    private List<ProductsDTO> products;
    private String originalTotalQuota;
    private String originalMonthQuota;

    public String getTotalUseCoupon() {
        return totalUseCoupon;
    }

    public void setTotalUseCoupon(String totalUseCoupon) {
        this.totalUseCoupon = totalUseCoupon;
    }

    public String getRemainTotalQuota() {
        return remainTotalQuota;
    }

    public void setRemainTotalQuota(String remainTotalQuota) {
        this.remainTotalQuota = remainTotalQuota;
    }

    public String getRemainMonthQuota() {
        return remainMonthQuota;
    }

    public void setRemainMonthQuota(String remainMonthQuota) {
        this.remainMonthQuota = remainMonthQuota;
    }

    public String getTotalUseMonthCoupon() {
        return totalUseMonthCoupon;
    }

    public void setTotalUseMonthCoupon(String totalUseMonthCoupon) {
        this.totalUseMonthCoupon = totalUseMonthCoupon;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductsDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsDTO> products) {
        this.products = products;
    }

    public String getOriginalTotalQuota() {
        return originalTotalQuota;
    }

    public void setOriginalTotalQuota(String originalTotalQuota) {
        this.originalTotalQuota = originalTotalQuota;
    }

    public String getOriginalMonthQuota() {
        return originalMonthQuota;
    }

    public void setOriginalMonthQuota(String originalMonthQuota) {
        this.originalMonthQuota = originalMonthQuota;
    }

    public static class ProductsDTO {
        private Integer productId;
        private Integer productNum;
        private Integer cartType;
        private Integer goodsId;
        private String singleUseCoupon;
        private ProductVODTO productVO;
        private GoodsVODTO goodsVO;


        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getProductNum() {
            return productNum;
        }

        public void setProductNum(Integer productNum) {
            this.productNum = productNum;
        }

        public Integer getCartType() {
            return cartType;
        }

        public void setCartType(Integer cartType) {
            this.cartType = cartType;
        }

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public String getSingleUseCoupon() {
            return singleUseCoupon;
        }

        public void setSingleUseCoupon(String singleUseCoupon) {
            this.singleUseCoupon = singleUseCoupon;
        }

        public ProductVODTO getProductVO() {
            return productVO;
        }

        public void setProductVO(ProductVODTO productVO) {
            this.productVO = productVO;
        }

        public GoodsVODTO getGoodsVO() {
            return goodsVO;
        }

        public void setGoodsVO(GoodsVODTO goodsVO) {
            this.goodsVO = goodsVO;
        }

        public static class ProductVODTO {
            private String productName;
            private String originalPrice;
            private String discount;

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(String originalPrice) {
                this.originalPrice = originalPrice;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }
        }

        public static class GoodsVODTO {
            private String price;
            private Integer goodsId;
            private Integer productId;
            private String name;
            private String description;
            private String imageUrl;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public Integer getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(Integer goodsId) {
                this.goodsId = goodsId;
            }

            public Integer getProductId() {
                return productId;
            }

            public void setProductId(Integer productId) {
                this.productId = productId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }
    }
}
