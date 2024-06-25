package com.yiqizhuan.app.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSON;
import com.gyf.immersionbar.ImmersionBar;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.AddressDefaultBean;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.CategoryFindByBean;
import com.yiqizhuan.app.bean.GoodsDetailBean;
import com.yiqizhuan.app.bean.ShopcartActionPara;
import com.yiqizhuan.app.bean.ShopcartPaymentConfirmBean;
import com.yiqizhuan.app.bean.UserCouponBean;
import com.yiqizhuan.app.databinding.ActivityGoodsDetailBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseActivity;
import com.yiqizhuan.app.ui.detail.adapter.DetailBannerAdapter;
import com.yiqizhuan.app.ui.detail.dialog.DetailBottomDialog;
import com.yiqizhuan.app.util.ClickUtil;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.util.UnreadMsgUtil;
import com.yiqizhuan.app.webview.WebActivity;
import com.youth.banner.listener.OnPageChangeListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-06-21 10:48 PM
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private String productId;
    private String type;
    ActivityGoodsDetailBinding binding;
    private GoodsDetailBean goodsDetailBean;
    private GoodsDetailBean.GoodsAndAttrMapping mapping;
    //是否选择了商品
    private boolean selectGoodsAndAttrMapping;
    private AddressDefaultBean addressDefaultBean;
    private UserCouponBean userCouponBean;
    DetailBottomDialog dialog;
    private boolean isClickShopping;
    private boolean isClickCommit;
    private int currentNum = 1;
    boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        binding = ActivityGoodsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId");
            type = getIntent().getStringExtra("type");
        }
        initView();
        queryUserCoupon();
//        shopCartCount();
    }

    private void request() {
        productDetail();
        categoryFindByProductId();
        addressDefault();
    }

    private void initView() {
        LiveEventBus.get("getAddressFormJs", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String id = "";
                String detailedAddress = "";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    id = jsonObject.getString("id");
                    detailedAddress = jsonObject.getString("address");
                    addressDefaultBean = new AddressDefaultBean();
                    addressDefaultBean.setId(id);
                    addressDefaultBean.setDetailedAddress(detailedAddress);
                    binding.tvAddress.setText(addressDefaultBean.getDetailedAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.ivBack.setOnClickListener(this);
        binding.llyShop.setOnClickListener(this);
        binding.tvJiaRuGoWuChe.setOnClickListener(this);
        binding.llyCommit.setOnClickListener(this);
        binding.rlyXuanZe.setOnClickListener(this);
        binding.rlyAddress.setOnClickListener(this);
        binding.llyShop.setOnClickListener(this);
        binding.banner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tvNunL.setText(position + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        if (goodsDetailBean != null) {
            binding.tvNunR.setText(goodsDetailBean.getDetailImages().size() + "");
            binding.banner.setAdapter(new DetailBannerAdapter(this, goodsDetailBean.getDetailImages()));
            binding.tvName.setText("           " + goodsDetailBean.getProductName());
            StringBuilder tag = new StringBuilder();
            if (goodsDetailBean.getTags() != null) {
                for (int i = 0; i < goodsDetailBean.getTags().size(); i++) {
                    tag.append(goodsDetailBean.getTags().get(i));
                    if (i < goodsDetailBean.getTags().size() - 1) {
                        tag.append(" · ");
                    }
                }
            }
            if (tag.length() > 0) {
                binding.tvDescribe.setText(tag.toString());
            } else {
                binding.tvDescribe.setText("");
            }
            if (goodsDetailBean.getDescriptionImages() != null && goodsDetailBean.getDescriptionImages().size() > 0) {
                GlideUtil.loadImageBig(goodsDetailBean.getDescriptionImages().get(0), binding.ivBig);
            }
            //取第一个商品库存大于0的，如果都没有取第一个
            boolean inventory = false;
            for (GoodsDetailBean.GoodsAndAttrMapping goodsAndAttrMapping : goodsDetailBean.getGoodsAndAttrMapping()) {
                if (!inventory) {
                    if (Integer.parseInt(goodsAndAttrMapping.getInventory()) > 0) {
                        mapping = goodsAndAttrMapping;
                        inventory = true;
                    }
                }
            }
            if (!inventory) {
                mapping = goodsDetailBean.getGoodsAndAttrMapping().get(0);
            }
            selectGoodsAndAttrMapping = true;
            //属性
            setMapping();
        }
    }

    private void setMapping() {
        isClickShopping = false;
        isClickCommit = false;
        String originalPrice = mapping.getPrice() + "元";
        SpannableString spannableString = new SpannableString(originalPrice);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.6f);
        spannableString.setSpan(relativeSizeSpan, originalPrice.length() - 1, originalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvOriginalPrice.setText(spannableString);
        binding.tvCommitPrice.setText("￥" + mapping.getGoodsSellPrice());
        //顶部价格
        if (TextUtils.equals(type, "1")) {
            String sellPrice = mapping.getDiscount() + " 积分 + " + mapping.getGoodsSellPrice() + " 元";
            SpannableString spannableString1 = new SpannableString(sellPrice);
            RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.6f);
            RelativeSizeSpan relativeSizeSpan2 = new RelativeSizeSpan(0.6f);
            spannableString1.setSpan(relativeSizeSpan1, goodsDetailBean.getDiscount().length(), goodsDetailBean.getDiscount().length() + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString1.setSpan(relativeSizeSpan2, sellPrice.length() - 1, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvPrice.setText(spannableString1);

            binding.ivRight.setImageResource(R.mipmap.ic_zunxianghui_right);
            binding.ivTitle.setImageResource(R.mipmap.ic_zunxianghui_title);
            binding.llyPrice.setVisibility(View.VISIBLE);
            binding.ivBgPrice.setVisibility(View.GONE);
            binding.tvChangGou.setVisibility(View.GONE);
        }
        //共享
        else if (TextUtils.equals(type, "2")) {
            binding.ivRight.setImageResource(R.mipmap.ic_gongxianghu_right);
            binding.ivTitle.setImageResource(R.mipmap.ic_gongxianghui_title);
            binding.llyPrice.setVisibility(View.GONE);
            binding.ivBgPrice.setVisibility(View.VISIBLE);
            binding.ivBgPrice.setImageResource(R.mipmap.ic_gongxianghezuo);
            binding.tvChangGou.setVisibility(View.GONE);
        }
        //悦享
        else if (TextUtils.equals(type, "3")) {
            binding.ivRight.setImageResource(R.mipmap.ic_yuexianghui_right);
            binding.ivTitle.setImageResource(R.mipmap.ic_yuxianghui_title);
            binding.llyPrice.setVisibility(View.GONE);
            binding.ivBgPrice.setVisibility(View.VISIBLE);
            binding.ivBgPrice.setImageResource(R.mipmap.ic_jifenquanedikou);
            binding.tvChangGou.setVisibility(View.VISIBLE);
            binding.tvOriginalPrice.getPaint().setStrikeThruText(true);
        }
        //畅享
        else if (TextUtils.equals(type, "4")) {
            binding.ivRight.setImageResource(R.mipmap.ic_changxianghui_right);
            binding.ivTitle.setImageResource(R.mipmap.ic_changxianghui_title);
            binding.llyPrice.setVisibility(View.GONE);
            binding.ivBgPrice.setVisibility(View.VISIBLE);
            binding.ivBgPrice.setImageResource(R.mipmap.ic_jifenquanedikou);
            binding.tvChangGou.setVisibility(View.VISIBLE);
            binding.tvOriginalPrice.getPaint().setStrikeThruText(true);
        }
        StringBuilder stringBuilder = new StringBuilder();
        //属性
        if (mapping.getAttrs() != null) {
            for (String string : mapping.getAttrs()) {
                if (goodsDetailBean.getAttributes() != null && goodsDetailBean.getAttributes().size() > 0) {
                    for (GoodsDetailBean.Attributes attributes : goodsDetailBean.getAttributes()) {
                        if (attributes.getAttrDescription() != null && attributes.getAttrDescription().size() > 0) {
                            for (GoodsDetailBean.AttrDescription attrDescription : attributes.getAttrDescription()) {
                                if (TextUtils.equals(string, attrDescription.getAttrId())) {
//                                    stringBuilder.append(attributes.getAttrName() + "，");
                                    stringBuilder.append(attrDescription.getAttrValue() + "，");
                                }
                            }
                        }
                    }
                }
            }
        }
        stringBuilder.append(currentNum + "件");
        if (stringBuilder.length() > 0) {
            binding.tvShuXing.setText(stringBuilder.toString());
        }
        //底部按钮
        if (Integer.parseInt(goodsDetailBean.getDeleted()) == 1) {
            binding.tvCommitPrice.setText("已下架");
            binding.tvXianShiYuGu.setVisibility(View.GONE);
            binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
            binding.tvJiaRuGoWuChe.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
            isClickShopping = false;
            isClickCommit = false;
        } else if (mapping.getStatus() == 0) {
            binding.tvCommitPrice.setText("待开售");
            binding.tvXianShiYuGu.setVisibility(View.GONE);
            binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
            binding.tvJiaRuGoWuChe.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
            isClickShopping = false;
            isClickCommit = false;
        } else if (mapping.getStatus() == 1 && (Integer.parseInt(mapping.getInventory()) == 0)) {
            binding.tvCommitPrice.setText("已售罄");
            binding.tvXianShiYuGu.setVisibility(View.GONE);
            binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
            binding.tvJiaRuGoWuChe.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
            isClickShopping = true;
            isClickCommit = true;
        } else {
            binding.tvJiaRuGoWuChe.setBackground(getDrawable(R.drawable.background_conner_ffd418_ffbc1f_21dp));
            isClickShopping = true;
            isClickCommit = true;
            //尊享
            if (TextUtils.equals(type, "1")) {
                binding.tvXianShiYuGu.setVisibility(View.VISIBLE);
                binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
                if (userCouponBean != null && userCouponBean.getData() != null && ((Double.parseDouble(mapping.getDiscount()) * currentNum) > Double.parseDouble(userCouponBean.getData().getTotalQuota()))) {
                    isClickCommit = true;
                    binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
                    binding.tvXianShiYuGu.setText("您的积分不足");
                }
            }
            //共享
            else if (TextUtils.equals(type, "2")) {
                binding.tvXianShiYuGu.setVisibility(View.GONE);
                binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
            }
            //悦享
            else if (TextUtils.equals(type, "3") || TextUtils.equals(type, "4")) {
                binding.tvXianShiYuGu.setVisibility(View.GONE);
                binding.tvDiKouXiao.setVisibility(View.VISIBLE);
                String jifen = "0元购";
                if (userCouponBean != null && userCouponBean.getData() != null && ((Double.parseDouble(mapping.getDiscount()) * currentNum) > Double.parseDouble(userCouponBean.getData().getTotalQuota()))) {
                    isClickCommit = true;
                    jifen = "您的积分不足";
                    binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_989898_989898_21dp));
                } else {
                    binding.llyCommit.setBackground(getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
                }
                binding.tvCommitPrice.setText(jifen);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            //购物车
            case R.id.llyShop:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LiveEventBus.get("shopping").post("");
                    }
                }, 100);
                finish();
                break;
            //地址
            case R.id.rlyAddress:
                Intent address = new Intent(this, WebActivity.class);
                address.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ADDRESS + "?source=detail");
                startActivity(address);
                break;
            case R.id.tvJiaRuGoWuChe:
                if (isClickShopping) {
                    if (ClickUtil.isRealClick()) {
                        showDialog();
                    }
                }
                break;
            case R.id.llyCommit:
                if (isClickCommit) {
                    if (ClickUtil.isRealClick()) {
                        showDialog();
                    }
                }
                break;
            case R.id.rlyXuanZe:
                if (ClickUtil.isRealClick()) {
                    showDialog();
                }
                break;
        }
    }

    private void showDialog() {
        dialog = new DetailBottomDialog(this);
        dialog.initData(goodsDetailBean, mapping, type, userCouponBean, selectGoodsAndAttrMapping, currentNum, new DetailBottomDialog.DialogListenerSure() {
            @Override
            public void onPositiveClick(GoodsDetailBean result, GoodsDetailBean.GoodsAndAttrMapping goodsAndAttrMapping, int num) {
                goodsDetailBean = result;
                mapping = goodsAndAttrMapping;
                currentNum = num;
                setMapping();
            }

            @Override
            public void shopcartAction(GoodsDetailBean result, GoodsDetailBean.GoodsAndAttrMapping goodsAndAttrMapping, int num) {
                goodsDetailBean = result;
                mapping = goodsAndAttrMapping;
                currentNum = num;
                addShopcartAction(num);
            }

            @Override
            public void addShopcartPaymentConfirm(GoodsDetailBean result, GoodsDetailBean.GoodsAndAttrMapping goodsAndAttrMapping, int num) {
                goodsDetailBean = result;
                mapping = goodsAndAttrMapping;
                currentNum = num;
                paymentConfirm(num);
            }
        });
        dialog.show();
    }

    private void productDetail() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("productId", productId);
        paramsMap.put("type", type);
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_DETAIL, paramsMap, new BaseCallBack<BaseResult<GoodsDetailBean>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<GoodsDetailBean> result) {
                if (result != null) {
                    goodsDetailBean = result.getData();
                    if (goodsDetailBean != null) {
                        if (!TextUtils.isEmpty(goodsDetailBean.getProductName())) {
                            goodsDetailBean.setProductName(goodsDetailBean.getProductName().replace("\n", ""));
                        }
                        if (!TextUtils.isEmpty(goodsDetailBean.getProductDescription())) {
                            goodsDetailBean.setProductDescription(goodsDetailBean.getProductDescription().replace("\n", ""));
                        }
                    }
                    initData();
                }
                cancelLoading();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    private void categoryFindByProductId() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.CATEGORY_FINDBYPRODUCTID + "/" + productId, paramsMap, new BaseCallBack<BaseResult<List<CategoryFindByBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<CategoryFindByBean>> result) {
                if (result != null && result.getData() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (CategoryFindByBean categoryFindByBean : result.getData()) {
                        stringBuilder.append(categoryFindByBean.getName() + " ");
                    }
                    binding.tvFenlei.setText(stringBuilder.toString());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    private void addressDefault() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.ADDRESS_DEFAULT, paramsMap, new BaseCallBack<BaseResult<AddressDefaultBean>>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<AddressDefaultBean> result) {
                if (result != null && result.getData() != null) {
                    addressDefaultBean = result.getData();
                    binding.tvAddress.setText(addressDefaultBean.getDetailedAddress());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    private void queryUserCoupon() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.QUERY_USER_COUPON, paramsMap, new BaseCallBack<UserCouponBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                request();
            }

            @Override
            public void onSuccess(Call call, Response response, UserCouponBean result) {
                userCouponBean = result;
                request();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                request();
            }
        });
    }

    /**
     * @param num 添加购物车
     */
    private void addShopcartAction(int num) {
        showLoading();
        List<ShopcartActionPara> strings = new ArrayList<>();
        ShopcartActionPara shopcartActionPara = new ShopcartActionPara();
        shopcartActionPara.setProductId(Integer.parseInt(productId));
        shopcartActionPara.setGoodsId(Integer.parseInt(mapping.getGoodsId()));
        shopcartActionPara.setCartType(Integer.parseInt(type));
        shopcartActionPara.setActionType(0);
        shopcartActionPara.setProductNum(num);
        strings.add(shopcartActionPara);
        OkHttpManager.getInstance().postRequestObject(Api.SHOPCART_ACTION, strings, new BaseCallBack<String>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, String result) {
                ToastUtils.showToast("已加入购物车");
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//                shopCartCount();
                cancelLoading();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    /**
     * 获取购物车数量
     */
    private void shopCartCount() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.SHOP_CART_COUNT, paramsMap, new BaseCallBack<BaseResult<String>>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<String> result) {
                if (result != null && !TextUtils.isEmpty(result.getData())) {
                    LiveEventBus.get("changeCartNum").post(Integer.parseInt(result.getData()));
                    if (Integer.parseInt(result.getData()) > 0) {
                        binding.tvBadgeNum.setVisibility(View.GONE);
                        UnreadMsgUtil.show(binding.tvBadgeNum, Integer.parseInt(result.getData()));
                    } else {
                        binding.tvBadgeNum.setVisibility(View.GONE);
                    }
                } else {
                    binding.tvBadgeNum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    /**
     * @param num 商品确认
     */
    private void paymentConfirm(int num) {
        showLoading();
        List<ShopcartPaymentConfirmBean> strings = new ArrayList<>();
        ShopcartPaymentConfirmBean bean = new ShopcartPaymentConfirmBean();
        bean.setProductNum(num + "");
        bean.setCartType(type);
        bean.setSingleUseCoupon(mapping.getDiscount());
        bean.setGoodsId(mapping.getGoodsId());
        bean.setProductId(productId);
        ShopcartPaymentConfirmBean.ProductVO productVO = new ShopcartPaymentConfirmBean.ProductVO();
        productVO.setProductName(goodsDetailBean.getProductName());
        productVO.setOriginalPrice(goodsDetailBean.getOriginalPrice());
        productVO.setDiscount(goodsDetailBean.getDiscount());
        bean.setProductVO(productVO);
        ShopcartPaymentConfirmBean.GoodsVO goodsVO = new ShopcartPaymentConfirmBean.GoodsVO();
        goodsVO.setPrice(mapping.getPrice());
        goodsVO.setName(goodsDetailBean.getProductName());
        goodsVO.setImageUrl(mapping.getGoodsImageUrl());
        goodsVO.setDescription(goodsDetailBean.getProductDescription());
        goodsVO.setGoodsId(mapping.getGoodsId());
        goodsVO.setProductId(productId);
        bean.setGoodsVO(goodsVO);
        strings.add(bean);
        OkHttpManager.getInstance().postRequestObject(Api.SHOPCART_PAYMENTCONFIRM, strings, new BaseCallBack<BaseResult<Object>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<Object> result) {
                cancelLoading();
                if (result != null && result.getData() != null) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Intent checkout = new Intent(GoodsDetailActivity.this, WebActivity.class);
                    if (addressDefaultBean != null) {
                        checkout.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_CHECKOUT + "?id=" + addressDefaultBean.getId());
                    } else {
                        checkout.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_CHECKOUT);
                    }

                    checkout.putExtra("data", JSON.toJSONString(result.getData()));
//                    Gson gson = new Gson();
//                    checkout.putExtra("data", gson.toJson(result.getData()));
                    startActivity(checkout);
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}
