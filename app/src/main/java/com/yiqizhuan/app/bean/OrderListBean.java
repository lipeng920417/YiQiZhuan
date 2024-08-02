package com.yiqizhuan.app.bean;

import java.util.List;

/**
 * @author LiPeng
 * @create 2024-07-31 9:51 PM
 */
public class OrderListBean {

    private Integer count;
    private List<OrdersDTO> orders;


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<OrdersDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersDTO> orders) {
        this.orders = orders;
    }

    public static class OrdersDTO {
        private Integer id;
        private String orderStatus;
        private Integer orderStatusCode;
        private Boolean canAfterSale;
        private UserAddressDTO userAddress;
        private String orderNumber;
        private String remark;
        private ConfirmDTO confirm;
        private List<ProductsDTO> products;
        private String createdAt;
        private String updatedAt;
        //倒计时秒
        private String expiredSeconds;

        public String getExpiredSeconds() {
            return expiredSeconds;
        }

        public void setExpiredSeconds(String expiredSeconds) {
            this.expiredSeconds = expiredSeconds;
        }

        private List<ShippersDTO> shippers;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public Integer getOrderStatusCode() {
            return orderStatusCode;
        }

        public void setOrderStatusCode(Integer orderStatusCode) {
            this.orderStatusCode = orderStatusCode;
        }

        public Boolean getCanAfterSale() {
            return canAfterSale;
        }

        public void setCanAfterSale(Boolean canAfterSale) {
            this.canAfterSale = canAfterSale;
        }

        public UserAddressDTO getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(UserAddressDTO userAddress) {
            this.userAddress = userAddress;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public ConfirmDTO getConfirm() {
            return confirm;
        }

        public void setConfirm(ConfirmDTO confirm) {
            this.confirm = confirm;
        }

        public List<ProductsDTO> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsDTO> products) {
            this.products = products;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<ShippersDTO> getShippers() {
            return shippers;
        }

        public void setShippers(List<ShippersDTO> shippers) {
            this.shippers = shippers;
        }

        public static class UserAddressDTO {
            private String recipientName;
            private String recipientPhone;
            private String detailedAddress;

            public String getRecipientName() {
                return recipientName;
            }

            public void setRecipientName(String recipientName) {
                this.recipientName = recipientName;
            }

            public String getRecipientPhone() {
                return recipientPhone;
            }

            public void setRecipientPhone(String recipientPhone) {
                this.recipientPhone = recipientPhone;
            }

            public String getDetailedAddress() {
                return detailedAddress;
            }

            public void setDetailedAddress(String detailedAddress) {
                this.detailedAddress = detailedAddress;
            }
        }

        public static class ConfirmDTO {
            private String totalUseCoupon;
            private String remainTotalQuota;
            private String remainMonthQuota;
            private String totalUseMonthCoupon;
            private String totalPrice;
            private String originalTotalQuota;
            private String originalMonthQuota;
            private boolean needCash;

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

            public boolean isNeedCash() {
                return needCash;
            }

            public void setNeedCash(boolean needCash) {
                this.needCash = needCash;
            }
        }

        public static class ProductsDTO {
            private Integer goodsId;
            private Integer productId;
            private Integer productType;
            private Integer productNum;
            private GoodsDtoDTO goodsDto;

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

            public Integer getProductType() {
                return productType;
            }

            public void setProductType(Integer productType) {
                this.productType = productType;
            }

            public Integer getProductNum() {
                return productNum;
            }

            public void setProductNum(Integer productNum) {
                this.productNum = productNum;
            }

            public GoodsDtoDTO getGoodsDto() {
                return goodsDto;
            }

            public void setGoodsDto(GoodsDtoDTO goodsDto) {
                this.goodsDto = goodsDto;
            }

            public static class GoodsDtoDTO {
                private Integer goodsId;
                private Integer productId;
                private String goodsSerial;
                private String price;
                private String name;
                private String discount;
                private String description;
                private String imageUrl;
                private Integer status;
                private Integer quota;
                private Integer defaultGoods;
                private Integer deleted;

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

                public String getGoodsSerial() {
                    return goodsSerial;
                }

                public void setGoodsSerial(String goodsSerial) {
                    this.goodsSerial = goodsSerial;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
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

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public Integer getStatus() {
                    return status;
                }

                public void setStatus(Integer status) {
                    this.status = status;
                }

                public Integer getQuota() {
                    return quota;
                }

                public void setQuota(Integer quota) {
                    this.quota = quota;
                }

                public Integer getDefaultGoods() {
                    return defaultGoods;
                }

                public void setDefaultGoods(Integer defaultGoods) {
                    this.defaultGoods = defaultGoods;
                }

                public Integer getDeleted() {
                    return deleted;
                }

                public void setDeleted(Integer deleted) {
                    this.deleted = deleted;
                }
            }
        }

        public static class ShippersDTO {
            private String shipperCode;
            private String shipperName;
            private String shipperNumber;
            private boolean showTakeOver;
            private List<ProductsDTO> goods;
            private Integer goodsNum;
            private String status;
            private TraceDTO trace;

            public String getShipperCode() {
                return shipperCode;
            }

            public void setShipperCode(String shipperCode) {
                this.shipperCode = shipperCode;
            }

            public String getShipperName() {
                return shipperName;
            }

            public void setShipperName(String shipperName) {
                this.shipperName = shipperName;
            }

            public String getShipperNumber() {
                return shipperNumber;
            }

            public void setShipperNumber(String shipperNumber) {
                this.shipperNumber = shipperNumber;
            }

            public boolean isShowTakeOver() {
                return showTakeOver;
            }

            public void setShowTakeOver(boolean showTakeOver) {
                this.showTakeOver = showTakeOver;
            }

            public List<ProductsDTO> getGoods() {
                return goods;
            }

            public void setGoods(List<ProductsDTO> goods) {
                this.goods = goods;
            }

            public Integer getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(Integer goodsNum) {
                this.goodsNum = goodsNum;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public TraceDTO getTrace() {
                return trace;
            }

            public void setTrace(TraceDTO trace) {
                this.trace = trace;
            }

            public static class TraceDTO {
                private String eBusinessID;
                private List<String> preCoordinates;
                private List<CoordinatesDTO> coordinates;
                private String shipperCode;
                private String logisticCode;
                private Boolean success;
                private String state;
                private String stateEx;
                private String location;
                private String deliveryManTel;
                private List<TracesDTO> traces;
                private String senderCityLatAndLng;
                private String receiverCityLatAndLng;
                private String cityIsNull;
                private String routeMapUrl;

                public String geteBusinessID() {
                    return eBusinessID;
                }

                public void seteBusinessID(String eBusinessID) {
                    this.eBusinessID = eBusinessID;
                }

                public List<String> getPreCoordinates() {
                    return preCoordinates;
                }

                public void setPreCoordinates(List<String> preCoordinates) {
                    this.preCoordinates = preCoordinates;
                }

                public List<CoordinatesDTO> getCoordinates() {
                    return coordinates;
                }

                public void setCoordinates(List<CoordinatesDTO> coordinates) {
                    this.coordinates = coordinates;
                }

                public String getShipperCode() {
                    return shipperCode;
                }

                public void setShipperCode(String shipperCode) {
                    this.shipperCode = shipperCode;
                }

                public String getLogisticCode() {
                    return logisticCode;
                }

                public void setLogisticCode(String logisticCode) {
                    this.logisticCode = logisticCode;
                }

                public Boolean getSuccess() {
                    return success;
                }

                public void setSuccess(Boolean success) {
                    this.success = success;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getStateEx() {
                    return stateEx;
                }

                public void setStateEx(String stateEx) {
                    this.stateEx = stateEx;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getDeliveryManTel() {
                    return deliveryManTel;
                }

                public void setDeliveryManTel(String deliveryManTel) {
                    this.deliveryManTel = deliveryManTel;
                }

                public List<TracesDTO> getTraces() {
                    return traces;
                }

                public void setTraces(List<TracesDTO> traces) {
                    this.traces = traces;
                }

                public String getSenderCityLatAndLng() {
                    return senderCityLatAndLng;
                }

                public void setSenderCityLatAndLng(String senderCityLatAndLng) {
                    this.senderCityLatAndLng = senderCityLatAndLng;
                }

                public String getReceiverCityLatAndLng() {
                    return receiverCityLatAndLng;
                }

                public void setReceiverCityLatAndLng(String receiverCityLatAndLng) {
                    this.receiverCityLatAndLng = receiverCityLatAndLng;
                }

                public String getCityIsNull() {
                    return cityIsNull;
                }

                public void setCityIsNull(String cityIsNull) {
                    this.cityIsNull = cityIsNull;
                }

                public String getRouteMapUrl() {
                    return routeMapUrl;
                }

                public void setRouteMapUrl(String routeMapUrl) {
                    this.routeMapUrl = routeMapUrl;
                }

                public static class CoordinatesDTO {
                    private String latAndLng;
                    private String location;

                    public String getLatAndLng() {
                        return latAndLng;
                    }

                    public void setLatAndLng(String latAndLng) {
                        this.latAndLng = latAndLng;
                    }

                    public String getLocation() {
                        return location;
                    }

                    public void setLocation(String location) {
                        this.location = location;
                    }
                }

                public static class TracesDTO {
                    private String acceptTime;
                    private String acceptStation;
                    private String location;
                    private String action;

                    public String getAcceptTime() {
                        return acceptTime;
                    }

                    public void setAcceptTime(String acceptTime) {
                        this.acceptTime = acceptTime;
                    }

                    public String getAcceptStation() {
                        return acceptStation;
                    }

                    public void setAcceptStation(String acceptStation) {
                        this.acceptStation = acceptStation;
                    }

                    public String getLocation() {
                        return location;
                    }

                    public void setLocation(String location) {
                        this.location = location;
                    }

                    public String getAction() {
                        return action;
                    }

                    public void setAction(String action) {
                        this.action = action;
                    }
                }
            }

        }
    }
}
