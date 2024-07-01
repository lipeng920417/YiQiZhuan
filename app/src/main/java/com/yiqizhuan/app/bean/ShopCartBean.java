package com.yiqizhuan.app.bean;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-06-28 3:44 PM
 */
public class ShopCartBean {

    private int total;
    private List<DetailsDTO> details;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DetailsDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsDTO> details) {
        this.details = details;
    }

    public static class DetailsDTO {
        private boolean select;
        private int state;
        private int userId;
        private int productId;
        private int productNum;
        private int cartType;
        private String createTime;
        private String updateTime;
        private int deleted;
        private int id;
        private int goodsId;
        private String totalUseCoupon;
        private String singleUseCoupon;
        private ProductVODTO productVO;
        private GoodsVODTO goodsVO;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getProductNum() {
            return productNum;
        }

        public void setProductNum(int productNum) {
            this.productNum = productNum;
        }

        public int getCartType() {
            return cartType;
        }

        public void setCartType(int cartType) {
            this.cartType = cartType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getTotalUseCoupon() {
            return totalUseCoupon;
        }

        public void setTotalUseCoupon(String totalUseCoupon) {
            this.totalUseCoupon = totalUseCoupon;
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
            private String serialNumber;
            private String productName;
            private String productDescription;
            private String originalPrice;
            private String discount;
            private String mainImage;
            private int deleted;
            private int displaySupplierName;
            private List<String> detailImages;
            private List<String> descriptionImages;

            public String getSerialNumber() {
                return serialNumber;
            }

            public void setSerialNumber(String serialNumber) {
                this.serialNumber = serialNumber;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductDescription() {
                return productDescription;
            }

            public void setProductDescription(String productDescription) {
                this.productDescription = productDescription;
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

            public String getMainImage() {
                return mainImage;
            }

            public void setMainImage(String mainImage) {
                this.mainImage = mainImage;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public int getDisplaySupplierName() {
                return displaySupplierName;
            }

            public void setDisplaySupplierName(int displaySupplierName) {
                this.displaySupplierName = displaySupplierName;
            }

            public List<String> getDetailImages() {
                return detailImages;
            }

            public void setDetailImages(List<String> detailImages) {
                this.detailImages = detailImages;
            }

            public List<String> getDescriptionImages() {
                return descriptionImages;
            }

            public void setDescriptionImages(List<String> descriptionImages) {
                this.descriptionImages = descriptionImages;
            }
        }

        public static class GoodsVODTO {
            private String price;
            private int goodsId;
            private int productId;
            private String name;
            private String discount;
            private String description;
            private int stock;
            private String imageUrl;
            private int status;
            private int deleted;
            private String quota;

            public String getQuota() {
                return quota;
            }

            public void setQuota(String quota) {
                this.quota = quota;
            }

            private List<AttributesDTO> attributes;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public List<AttributesDTO> getAttributes() {
                return attributes;
            }

            public void setAttributes(List<AttributesDTO> attributes) {
                this.attributes = attributes;
            }

            public static class AttributesDTO {
                private String attrName;
                private List<AttrDescriptionDTO> attrDescription;

                public String getAttrName() {
                    return attrName;
                }

                public void setAttrName(String attrName) {
                    this.attrName = attrName;
                }

                public List<AttrDescriptionDTO> getAttrDescription() {
                    return attrDescription;
                }

                public void setAttrDescription(List<AttrDescriptionDTO> attrDescription) {
                    this.attrDescription = attrDescription;
                }

                public static class AttrDescriptionDTO {
                    private int attrId;
                    private String attrValue;

                    public int getAttrId() {
                        return attrId;
                    }

                    public void setAttrId(int attrId) {
                        this.attrId = attrId;
                    }

                    public String getAttrValue() {
                        return attrValue;
                    }

                    public void setAttrValue(String attrValue) {
                        this.attrValue = attrValue;
                    }
                }
            }
        }
    }
}
