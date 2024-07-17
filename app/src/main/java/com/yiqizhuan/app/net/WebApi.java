package com.yiqizhuan.app.net;

/**
 * @author LiPeng
 * @create 2024-04-23 6:24 PM
 */
public class WebApi {
    public static final String WEB_ADDRESS = "/address";
    public static final String WEB_ORDER = "/order";
    /**
     * productId=1&type=2
     * type传以下
     * 1：尊享
     * 2：共享
     * 3：悦享
     * 4：畅享
     */
    public static final String WEB_GOODS = "/goods";
    public static final String WEB_CART= "/cart";
    public static final String SERVICE_AGREEMENT= "/service/agreement";
    public static final String PRIVACY_AGREEMENT= "/privacy/agreement";

    /**
     * 尊享汇
     */
    public static final String WEB_LIST_ONE= "/list?type=1";

    /**
     * 共享汇
     */
    public static final String WEB_SHARED= "/list?type=2";

    /**
     * 悦享汇
     */
    public static final String web_list_three= "/list?type=3";

    /**
     * 畅享汇
     */
    public static final String WEB_LIST_FOUR= "/list?type=4";
    /**
     * 金刚区
     */
    public static final String WEB_CATEGORY_LIST= "/categoryList";

    /**
     * 确认兑换页
     */
    public static final String WEB_SIGN_AGREEMENT= "/signAgreement";

    /**
     * 下单页
     */
    public static final String WEB_CHECKOUT= "/checkout";

}
