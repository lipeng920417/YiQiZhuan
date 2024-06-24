package com.yiqizhuan.app.bean;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-06-22 3:58 PM
 * 商品详情
 */
public class GoodsDetailBean {
    private String serialNumber;
    private String originalPrice;
    private String productId;
    private String discount;
    private String sellPrice;
    private String totalInventory;
    private String productName;
    private String mainImage;
    private String deleted;
    private String displaySupplierName;
    private String productDescription;
    private String productType;
    private List<GoodsAndAttrMapping> goodsAndAttrMapping;
    private List<String> detailImages;
    private List<Attributes> attributes;
    private List<String> descriptionImages;
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(String totalInventory) {
        this.totalInventory = totalInventory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDisplaySupplierName() {
        return displaySupplierName;
    }

    public void setDisplaySupplierName(String displaySupplierName) {
        this.displaySupplierName = displaySupplierName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public List<GoodsAndAttrMapping> getGoodsAndAttrMapping() {
        return goodsAndAttrMapping;
    }

    public void setGoodsAndAttrMapping(List<GoodsAndAttrMapping> goodsAndAttrMapping) {
        this.goodsAndAttrMapping = goodsAndAttrMapping;
    }

    public List<String> getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(List<String> detailImages) {
        this.detailImages = detailImages;
    }

    public List<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attributes> attributes) {
        this.attributes = attributes;
    }

    public List<String> getDescriptionImages() {
        return descriptionImages;
    }

    public void setDescriptionImages(List<String> descriptionImages) {
        this.descriptionImages = descriptionImages;
    }

    public static class GoodsAndAttrMapping {
        private boolean virtual;
        private String goodsId;
        private String price;
        private String goodsSellPrice;
        private String goodsImageUrl;
        private String inventory;
        private List<String> attrs;
        //0代表待开售，1代表已开售 库存为0
        private int status;
        private String discount;

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isVirtual() {
            return virtual;
        }

        public void setVirtual(boolean virtual) {
            this.virtual = virtual;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoodsSellPrice() {
            return goodsSellPrice;
        }

        public void setGoodsSellPrice(String goodsSellPrice) {
            this.goodsSellPrice = goodsSellPrice;
        }

        public String getGoodsImageUrl() {
            return goodsImageUrl;
        }

        public void setGoodsImageUrl(String goodsImageUrl) {
            this.goodsImageUrl = goodsImageUrl;
        }

        public String getInventory() {
            return inventory;
        }

        public void setInventory(String inventory) {
            this.inventory = inventory;
        }

        public List<String> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<String> attrs) {
            this.attrs = attrs;
        }
    }

    public static class Attributes {
        private int pos;
        private String attrName;
        private List<AttrDescription> attrDescription;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public List<AttrDescription> getAttrDescription() {
            return attrDescription;
        }

        public void setAttrDescription(List<AttrDescription> attrDescription) {
            this.attrDescription = attrDescription;
        }
    }

    public static class AttrDescription {
        private String attrId;
        private String attrValue;
        //0为选中，1选中，2不可选
        private int select;
        private List<String> goodsIds;

        public int getSelect() {
            return select;
        }

        public void setSelect(int select) {
            this.select = select;
        }

        public String getAttrId() {
            return attrId;
        }

        public void setAttrId(String attrId) {
            this.attrId = attrId;
        }

        public String getAttrValue() {
            return attrValue;
        }

        public void setAttrValue(String attrValue) {
            this.attrValue = attrValue;
        }

        public List<String> getGoodsIds() {
            return goodsIds;
        }

        public void setGoodsIds(List<String> goodsIds) {
            this.goodsIds = goodsIds;
        }
    }



}

