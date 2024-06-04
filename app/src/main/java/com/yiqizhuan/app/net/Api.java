package com.yiqizhuan.app.net;

/**
 * @author LiPeng
 * @create 2024-04-22 8:45 PM
 */
public class Api {
    //获取验证码验证码
    public static final String CAPTCHA_SEND = "/mall/auth/captcha/send";
    //登录
    public static final String LOGIN_CAPTCHA = "/mall/auth/login/captcha";
    //退出登录
    public static final String AUTH_LOGOUT = "/mall/auth/logout";

    //注销账号
    public static final String TERMINATE_ACCOUNT = "/mall/auth/terminate/account";

    //查询用户信息
    public static final String USER_INFO = "/mall/user/info";

    //修改用户信息
    public static final String USER_SAVE = "/mall/user/save";

    //查询商品列表
    public static final String PRODUCT_LIST = "/product/list";

    //点击问号查询历史商券余额
    public static final String COUPON_INFO = "/coupon/info";

    //创购中心
    public static final String QUERY_USER_COUPON = "/coupon/queryUserCoupon";

    //默认商品
    public static final String PRODUCT_DEFAULT = "/product/default";

    //本周热销
    public static final String PRODUCT_BESTSELLERS = "/product/bestsellers";

    //查询购物车数量
    public static final String SHOP_CART_COUNT = "/shopcart/count";

    //累计兑换查询
    public static final String GET_HISTORY_EXCHANGE = "/points/getHistoryExchange";

    //积分明细查询
    public static final String HISTORY_DETAIL = "/points/historyDetail";

    //获取 类目列表
    public static final String CATEGORY_DEFAULT = "/category/default";
    //获取 类目立减 图标
    public static final String CATEGORY_GETDISCOUNTCATEGORY = "/category/getDiscountCategory";


}
