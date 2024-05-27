package com.yiqizhuan.app.net;

/**
 * @author LiPeng
 * @create 2024-04-22 8:45 PM
 */
public class Api {
    //获取验证码验证码
    public static final String CAPTCHA_SEND = "/api/mall/auth/captcha/send";
    //登录
    public static final String LOGIN_CAPTCHA = "/api/mall/auth/login/captcha";
    //退出登录
    public static final String AUTH_LOGOUT = "/api/mall/auth/logout";

    //注销账号
    public static final String TERMINATE_ACCOUNT = "/api/mall/auth/terminate/account";

    //查询用户信息
    public static final String USER_INFO = "/api/mall/user/info";

    //修改用户信息
    public static final String USER_SAVE = "/api/mall/user/save";

    //查询商品列表
    public static final String PRODUCT_LIST = "/api/product/list";

    //点击问号查询历史商券余额
    public static final String COUPON_INFO = "/api/coupon/info";

    //创购中心
    public static final String QUERY_USER_COUPON = "/api/coupon/queryUserCoupon";

    //默认商品
    public static final String PRODUCT_DEFAULT = "/api/product/default";

    //本周热销
    public static final String PRODUCT_BESTSELLERS = "/api/product/bestsellers";

    //查询购物车数量
    public static final String SHOP_CART_COUNT = "/api/shopcart/count";

}
