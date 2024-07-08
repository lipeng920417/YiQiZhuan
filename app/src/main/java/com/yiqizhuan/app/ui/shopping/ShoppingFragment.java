package com.yiqizhuan.app.ui.shopping;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.AddressDefaultBean;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.PaymentConfirmBean;
import com.yiqizhuan.app.bean.ShopCartBean;
import com.yiqizhuan.app.bean.ShopcartActionPara;
import com.yiqizhuan.app.bean.ShopcartPaymentConfirmBean;
import com.yiqizhuan.app.databinding.FragmentShoppingBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.shopping.adapter.TabShoppingFragmentAdapter;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.util.ToastUtils;
import com.yiqizhuan.app.webview.WebActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ShoppingFragment extends BaseFragment implements View.OnClickListener {

    private FragmentShoppingBinding binding;
    private List<ShoppingTabFragment> fragments;
    private List<String> title;
    private TabShoppingFragmentAdapter tabFragmentAdapter;
    private ShoppingTabFragment shoppingTabFragment;
    private AddressDefaultBean addressDefaultBean;
    private List<ShopCartBean.DetailsDTO> details = new ArrayList<>();
    private int state;
    private List<ShopCartBean.DetailsDTO> selectData = new ArrayList<>();
    private PaymentConfirmBean commitObject;
    private boolean jiFenBuZu;
    private boolean allSelect;
    private int type;
    private boolean hidden;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        initView();
        return root;
    }

    private void initView() {
        binding.llyAddress.setOnClickListener(this);
        binding.tvRightBtn.setOnClickListener(this);
        binding.tvCommit.setOnClickListener(this);
        binding.tvDelete.setOnClickListener(this);
        binding.llyAllSelect.setOnClickListener(this);
        initTab();
        LiveEventBus.get("getAddressFormJs", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                try {
                    addressDefaultBean = new AddressDefaultBean();
                    JSONObject jsonObject = new JSONObject(s);
                    if ("shopCart".equals(jsonObject.getString("source"))) {
                        addressDefaultBean.setId(jsonObject.getString("id"));
                        addressDefaultBean.setDetailedAddress(jsonObject.getString("detailedAddress"));
                        addressDefaultBean.setProvince(jsonObject.getString("province"));
                        addressDefaultBean.setCity(jsonObject.getString("city"));
                        addressDefaultBean.setCounty(jsonObject.getString("county"));
                        binding.tvAddress.setText(addressDefaultBean.getProvince() + addressDefaultBean.getCity() + addressDefaultBean.getCounty() + addressDefaultBean.getDetailedAddress());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        setPrice("0", "0", "0");
    }

    private void setPrice(String sellPrice, String discount, String remainTotalQuota) {
        binding.tvMoney.setText(getMoney(sellPrice));
        binding.tvDiscount.setText(getDiscount(discount));
        if (selectData.size() > 0) {
            if (Double.parseDouble(remainTotalQuota) < 0) {
                binding.tvCommit.setBackground(getActivity().getResources().getDrawable(R.drawable.background_conner_989898_989898_21dp));
                binding.tvCommit.setText("积分不足");
                binding.rlyJifenquane.setVisibility(View.GONE);
                jiFenBuZu = true;
            } else {
                boolean yueChang = true;
                for (ShopCartBean.DetailsDTO detailsDTO : selectData) {
                    if (detailsDTO.getCartType() == 1 || detailsDTO.getCartType() == 2) {
                        yueChang = false;
                    }
                }
                binding.tvCommit.setBackground(getActivity().getResources().getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
                if (yueChang) {
                    binding.tvCommit.setText("0元购");
                    if (state == 0) {
                        binding.rlyJifenquane.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.tvCommit.setText("去结算");
                    binding.rlyJifenquane.setVisibility(View.GONE);
                }
                jiFenBuZu = false;
            }
        } else {
            binding.tvCommit.setBackground(getActivity().getResources().getDrawable(R.drawable.background_conner_ff404f_fa2c19_21dp));
            binding.tvCommit.setText("去结算");
            binding.rlyJifenquane.setVisibility(View.GONE);
            jiFenBuZu = false;
        }
    }

    private SpannableString getMoney(String sellPrice) {
        int sellPriceNum = getDecimalPlaces(sellPrice);
        SpannableString spannableString1 = new SpannableString(sellPrice);
        RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.7f);
        spannableString1.setSpan(relativeSizeSpan1, sellPrice.length() - sellPriceNum, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString1;
    }

    private SpannableString getDiscount(String discount) {
        String sellPrice = discount;
        int sellPriceNum = getDecimalPlaces(sellPrice);
        SpannableString spannableString1 = new SpannableString(sellPrice);
        RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(0.7f);
        spannableString1.setSpan(relativeSizeSpan1, sellPrice.length() - sellPriceNum, sellPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString1;
    }

    private int getDecimalPlaces(String number) {
        if (number.contains(".")) {
            int indexOfDecimal = number.indexOf(".");
            return number.length() - indexOfDecimal - 1;
        } else {
            return 0; // 如果字符串中不包含小数点，则小数位数为0
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.llyAddress:
                Intent address = new Intent(getActivity(), WebActivity.class);
                address.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_ADDRESS + "?source=shopCart");
                startActivity(address);
                break;
            case R.id.tvRightBtn:
                if (state == 0) {
                    binding.tvRightBtn.setText("完成");
                    state = 1;
                    binding.tvCommit.setVisibility(View.GONE);
                    binding.tvDelete.setVisibility(View.VISIBLE);
                    binding.tvNum.setVisibility(View.GONE);
                    binding.llyMoney.setVisibility(View.INVISIBLE);
                    if (details != null) {
                        for (ShopCartBean.DetailsDTO detailsDTO : details) {
                            detailsDTO.setState(state);
                        }
                    }
                    for (ShoppingTabFragment shoppingTabFragment : fragments) {
                        if (shoppingTabFragment != null) {
                            shoppingTabFragment.refresh();
                        }
                    }
                } else {
                    binding.tvRightBtn.setText("管理");
                    state = 0;
                    binding.tvCommit.setVisibility(View.VISIBLE);
                    binding.tvDelete.setVisibility(View.GONE);
                    binding.tvNum.setVisibility(View.VISIBLE);
                    binding.llyMoney.setVisibility(View.VISIBLE);
                    if (selectData != null) {
                        for (ShopCartBean.DetailsDTO detailsDTO : selectData) {
                            if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1 || detailsDTO.getGoodsVO().getStatus() == 0 || detailsDTO.getGoodsVO().getStock() == 0) {
                                detailsDTO.setSelect(false);
                            }
                        }
                    }
                    if (details != null) {
                        for (ShopCartBean.DetailsDTO detailsDTO : details) {
                            detailsDTO.setState(state);
                            if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1 || detailsDTO.getGoodsVO().getStatus() == 0 || detailsDTO.getGoodsVO().getStock() == 0) {
                                detailsDTO.setSelect(false);
                            }
                        }
                    }
                    refreshFragment();
                }
                break;
            case R.id.tvCommit:
                if (selectData == null || selectData.size() == 0) {
                    ToastUtils.showToast("请选择商品");
                    return;
                }
                if (jiFenBuZu) {
                    return;
                }
                paymentConfirm(true);
                break;
            case R.id.tvDelete:
                if (selectData == null || selectData.size() == 0) {
                    ToastUtils.showToast("请选择商品");
                    return;
                }
                List<ShopcartActionPara> shopCartActionParas = new ArrayList<>();
                for (ShopCartBean.DetailsDTO detailsDTO : selectData) {
                    ShopcartActionPara shopcartActionPara = new ShopcartActionPara();
                    shopcartActionPara.setId(detailsDTO.getId());
                    shopcartActionPara.setProductId(detailsDTO.getProductId());
                    shopcartActionPara.setGoodsId(detailsDTO.getGoodsId());
                    shopcartActionPara.setCartType(detailsDTO.getCartType());
                    shopcartActionPara.setActionType(1);
                    shopcartActionPara.setProductNum(detailsDTO.getProductNum());
                    shopCartActionParas.add(shopcartActionPara);
                }
                addShopCartAction(1, shopCartActionParas, false);
                break;
            case R.id.llyAllSelect:
                if (allSelect) {
                    allSelect = false;
                } else {
                    allSelect = true;
                }
                setAllSelect();
                break;
        }
    }

    private void goCommitObject() {
        if (commitObject != null) {
            Intent checkout = new Intent(getActivity(), WebActivity.class);
            if (addressDefaultBean != null) {
                checkout.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_CHECKOUT + "?id=" + addressDefaultBean.getId() + "&from=2");
            } else {
                checkout.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_CHECKOUT + "?from=2");
            }

            checkout.putExtra("data", JSON.toJSONString(commitObject));
            startActivity(checkout);
        }
    }


    private void initTab() {
        fragments = new ArrayList<>();
        title = new ArrayList<>();
        title.add("全部");
        title.add("尊享汇");
        title.add("悦享汇");
        title.add("畅享汇");
        title.add("共享汇");
        for (String s : title) {
            Bundle bundle = new Bundle();
            if ("全部".equals(s)) {
                bundle.putString("type", "0");
            } else if ("尊享汇".equals(s)) {
                bundle.putString("type", "1");
            } else if ("悦享汇".equals(s)) {
                bundle.putString("type", "3");
            } else if ("畅享汇".equals(s)) {
                bundle.putString("type", "4");
            } else if ("共享汇".equals(s)) {
                bundle.putString("type", "2");
            }
            shoppingTabFragment = new ShoppingTabFragment();
            shoppingTabFragment.setShopCartListenerSure(new ShoppingTabFragment.ShopCartListenerSure() {
                @Override
                public void add(ShopCartBean.DetailsDTO detailsDTO, int num) {
                    addShopCartAction(detailsDTO, num);
                }

                @Override
                public void subtraction(ShopCartBean.DetailsDTO detailsDTO, int num) {
                    addShopCartAction(detailsDTO, num);
                }

                @Override
                public void select() {
                    refreshFragment();
                }
            });
            shoppingTabFragment.setArguments(bundle);
            fragments.add(shoppingTabFragment);
        }
        tabFragmentAdapter = new TabShoppingFragmentAdapter(getChildFragmentManager(), fragments, title, getContext());
        // 设置预加载Fragment个数
        binding.vp.setOffscreenPageLimit(fragments.size());
        binding.vp.setAdapter(tabFragmentAdapter);
        // 设置当前显示标签页为第一页
        binding.vp.setCurrentItem(0);
        // 将ViewPager和TabLayout绑定R
        binding.tab.setupWithViewPager(binding.vp);
        // 设置自定义tab
        for (int i = 0; i < binding.tab.getTabCount(); i++) {
            TabLayout.Tab tabAt = binding.tab.getTabAt(i);
            if (tabAt != null) {
                tabAt.setCustomView(tabFragmentAdapter.getTabView(i));
            }
        }
        // 设置第一页为选中状态
        View view = binding.tab.getTabAt(0).getCustomView();
        setTabStyle(view, true);
        type = 0;
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    type = 0;
                } else if (tab.getPosition() == 1) {
                    type = 1;
                } else if (tab.getPosition() == 2) {
                    type = 3;
                } else if (tab.getPosition() == 3) {
                    type = 4;
                } else if (tab.getPosition() == 4) {
                    type = 2;
                }
                isAllSelect();
                View view = tab.getCustomView();
                setTabStyle(view, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                setTabStyle(view, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void isAllSelect() {
        boolean select = true;
        if (details != null && details.size() > 0) {
            for (ShopCartBean.DetailsDTO detailsDTO : details) {
                if (type == 0) {
                    if (state == 0) {
                        if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1 || detailsDTO.getGoodsVO().getStatus() == 0 || detailsDTO.getGoodsVO().getStock() == 0) {
                        } else {
                            if (!detailsDTO.isSelect()) {
                                select = false;
                            }
                        }
                    } else {
                        if (!detailsDTO.isSelect()) {
                            select = false;
                        }
                    }
                } else {
                    if (detailsDTO.getCartType() == type) {
                        if (state == 0) {
                            if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1 || detailsDTO.getGoodsVO().getStatus() == 0 || detailsDTO.getGoodsVO().getStock() == 0) {
                            } else {
                                if (!detailsDTO.isSelect()) {
                                    select = false;
                                }
                            }
                        } else {
                            if (!detailsDTO.isSelect()) {
                                select = false;
                            }
                        }
                    }
                }
            }
        } else {
            select = false;
        }
        if (select) {
            binding.ivAllSelect.setImageResource(R.mipmap.ic_checkbox_select);
            allSelect = true;
        } else {
            binding.ivAllSelect.setImageResource(R.mipmap.ic_checkbox);
            allSelect = false;
        }
    }

    private void setAllSelect() {
        if (details != null) {
            for (ShopCartBean.DetailsDTO detailsDTO : details) {
                if (type == 0) {
                    detailsDTO.setSelect(allSelect);
                } else {
                    if (detailsDTO.getCartType() == type) {
                        detailsDTO.setSelect(allSelect);
                    }
                }
            }
        }
        refreshFragment();
    }

    private void refreshItemFragment(boolean select, int goodsId) {
        for (ShoppingTabFragment shoppingTabFragment : fragments) {
            if (shoppingTabFragment != null) {
                shoppingTabFragment.refreshItem(goodsId);
            }
        }
        if (select) {
            refreshFragmentCommon();
        }
    }

    private void refreshMultiItemFragment(boolean select, List<PaymentConfirmBean.ProductsDTO> products) {
        for (ShoppingTabFragment shoppingTabFragment : fragments) {
            if (shoppingTabFragment != null) {
                shoppingTabFragment.refreshMultiItem(products);
            }
        }
        if (select) {
            refreshFragmentCommon();
        }
    }


    private void refreshFragment() {
        for (ShoppingTabFragment shoppingTabFragment : fragments) {
            if (shoppingTabFragment != null) {
                shoppingTabFragment.refresh();
            }
        }
        refreshFragmentCommon();
    }

    private void refreshFragmentCommon() {

        selectData.clear();
        if (details != null) {
            for (ShopCartBean.DetailsDTO detailsDTO : details) {
                if (detailsDTO.isSelect()) {
                    if (state == 0) {
                        if (detailsDTO.getGoodsVO().getDeleted() == 1 || detailsDTO.getProductVO().getDeleted() == 1 || detailsDTO.getGoodsVO().getStatus() == 0 || detailsDTO.getGoodsVO().getStock() == 0) {
                        } else {
                            selectData.add(detailsDTO);
                        }
                    } else {
                        selectData.add(detailsDTO);
                    }
                }
            }
        }
        bottomView();
        isAllSelect();
        if (state == 0) {
            paymentConfirm(false);
        }
    }

    private void bottomView() {
        if (selectData != null && selectData.size() > 0) {
            binding.tvNum.setText("已选" + selectData.size() + "件,合计:");
            if (state == 0) {
                binding.llyMoney.setVisibility(View.VISIBLE);
            } else {
                binding.llyMoney.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.tvNum.setText("");
            binding.llyMoney.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            shopCartList();
            addressDefault();
            LiveEventBus.get("changeCartNum").post("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            shopCartList();
            LiveEventBus.get("changeCartNum").post("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setTabStyle(View view, boolean b) {
        if (view == null) {
            return;
        }
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        RelativeLayout rly = view.findViewById(R.id.rly);
        if (b) {
            tvTitle.setTextColor(getResources().getColor(R.color.color_ff1804));
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvTitle.setTextSize(16f);
        } else {
            tvTitle.setTextColor(getResources().getColor(R.color.color_333333));
            tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvTitle.setTextSize(14f);
        }
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
                    binding.tvAddress.setText(addressDefaultBean.getProvince() + addressDefaultBean.getCity() + addressDefaultBean.getCounty() + addressDefaultBean.getDetailedAddress());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    /**
     * 查询个人购物车
     */
    private void shopCartList() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", "");
        paramsMap.put("page", "1");
        paramsMap.put("size", "300");
        OkHttpManager.getInstance().getRequest(Api.SHOPCART_LIST, paramsMap, new BaseCallBack<BaseResult<ShopCartBean>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<ShopCartBean> result) {
                if (result != null && result.getData() != null && result.getData().getDetails() != null && result.getData().getDetails().size() > 0) {
                    details = result.getData().getDetails();
                    List<ShopCartBean.DetailsDTO> newSelectData = new ArrayList<>();
                    if (selectData != null && selectData.size() > 0) {
                        for (ShopCartBean.DetailsDTO detailsDTO : selectData) {
                            boolean select = false;
                            for (ShopCartBean.DetailsDTO detailsDTO1 : details) {
                                if (detailsDTO1.getGoodsId() == detailsDTO.getGoodsId()) {
                                    select = true;
                                }
                            }
                            if (select) {
                                newSelectData.add(detailsDTO);
                            }
                        }
                    }
                    selectData.clear();
                    selectData.addAll(newSelectData);
                    for (ShopCartBean.DetailsDTO detailsDTO : details) {
                        detailsDTO.setState(state);
                        if (!TextUtils.isEmpty(detailsDTO.getProductVO().getProductName())) {
                            detailsDTO.getProductVO().setProductName(detailsDTO.getProductVO().getProductName().replace("\n", ""));
                        }
                        if (!TextUtils.isEmpty(detailsDTO.getProductVO().getProductDescription())) {
                            detailsDTO.getProductVO().setProductDescription(detailsDTO.getProductVO().getProductDescription().replace("\n", ""));
                        }

                        if (!TextUtils.isEmpty(detailsDTO.getGoodsVO().getName())) {
                            detailsDTO.getGoodsVO().setName(detailsDTO.getGoodsVO().getName().replace("\n", ""));
                        }
                        if (!TextUtils.isEmpty(detailsDTO.getGoodsVO().getDescription())) {
                            detailsDTO.getGoodsVO().setDescription(detailsDTO.getGoodsVO().getDescription().replace("\n", ""));
                        }
                        boolean select = false;
                        for (ShopCartBean.DetailsDTO detailsDTO1 : selectData) {
                            if (detailsDTO1.getGoodsId() == detailsDTO.getGoodsId()) {
                                select = true;
                            }
                        }
                        if (select) {
                            detailsDTO.setSelect(true);
                        }
                    }
                    for (ShoppingTabFragment shoppingTabFragment : fragments) {
                        if (shoppingTabFragment != null) {
                            shoppingTabFragment.initData(details);
                        }
                    }
                    bottomView();
                    isAllSelect();
                    paymentConfirm(false);
                } else {
                    details.clear();
                    selectData.clear();
                    binding.ivAllSelect.setImageResource(R.mipmap.ic_checkbox);
                    allSelect = false;
                    bottomView();
                    for (ShoppingTabFragment shoppingTabFragment : fragments) {
                        if (shoppingTabFragment != null) {
                            shoppingTabFragment.initData(details);
                        }
                    }
                }
                cancelLoading();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    private void addShopCartAction(ShopCartBean.DetailsDTO detailsDTO, int num) {
        List<ShopcartActionPara> shopCartActionParas = new ArrayList<>();
        ShopcartActionPara shopcartActionPara = new ShopcartActionPara();
        shopcartActionPara.setId(detailsDTO.getId());
        shopcartActionPara.setProductId(detailsDTO.getProductId());
        shopcartActionPara.setGoodsId(detailsDTO.getGoodsId());
        shopcartActionPara.setCartType(detailsDTO.getCartType());
        shopcartActionPara.setActionType(2);
        shopcartActionPara.setProductNum(num);
        shopCartActionParas.add(shopcartActionPara);
        addShopCartAction(2, shopCartActionParas, detailsDTO.isSelect());
    }


    /**
     * @param actionType 操作类型 0 添加 1删除 2 编辑（加减商品数量）
     */
    private void addShopCartAction(int actionType, List<ShopcartActionPara> shopCartActionParas, boolean isSelect) {
        showLoading();
        OkHttpManager.getInstance().postRequestObject(Api.SHOPCART_ACTION, shopCartActionParas, new BaseCallBack<String>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, String result) {
                selectData.clear();
                cancelLoading();
                if (actionType == 1) {
                    shopCartList();
                    LiveEventBus.get("changeCartNum").post("");
                    ToastUtils.showToast("删除成功");
                } else if (actionType == 2) {
                    boolean select = false;
                    if (details != null) {
                        for (ShopCartBean.DetailsDTO detailsDTO : details) {
                            if (detailsDTO.getGoodsId() == shopCartActionParas.get(0).getGoodsId()) {
                                detailsDTO.setProductNum(shopCartActionParas.get(0).getProductNum());
                                if (detailsDTO.isSelect()) {
                                    select = true;
                                }
                            }
                        }
                    }
                    refreshItemFragment(select, shopCartActionParas.get(0).getGoodsId());
                    ToastUtils.showToast("编辑成功");
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    /**
     * 商品确认
     */
    private void paymentConfirm(boolean payment) {
        if (selectData != null && selectData.size() > 0) {
            showLoading();
            List<ShopcartPaymentConfirmBean> shopCartPaymentConfirmBeans = new ArrayList<>();
            for (ShopCartBean.DetailsDTO detailsDTO : selectData) {
                ShopcartPaymentConfirmBean bean = new ShopcartPaymentConfirmBean();
                bean.setProductNum(detailsDTO.getProductNum() + "");
                bean.setCartType(detailsDTO.getCartType() + "");
                bean.setSingleUseCoupon(detailsDTO.getGoodsVO().getDiscount());
                bean.setGoodsId(detailsDTO.getGoodsId() + "");
                bean.setProductId(detailsDTO.getProductId() + "");
                ShopcartPaymentConfirmBean.ProductVO productVO = new ShopcartPaymentConfirmBean.ProductVO();
                productVO.setProductName(detailsDTO.getProductVO().getProductName());
                productVO.setOriginalPrice(detailsDTO.getProductVO().getOriginalPrice());
                productVO.setDiscount(detailsDTO.getProductVO().getDiscount());
                bean.setProductVO(productVO);
                ShopcartPaymentConfirmBean.GoodsVO goodsVO = new ShopcartPaymentConfirmBean.GoodsVO();
                goodsVO.setPrice(detailsDTO.getGoodsVO().getPrice());
                goodsVO.setName(detailsDTO.getGoodsVO().getName());
                goodsVO.setImageUrl(detailsDTO.getGoodsVO().getImageUrl());
                goodsVO.setDescription(detailsDTO.getGoodsVO().getDescription());
                goodsVO.setGoodsId(detailsDTO.getGoodsId() + "");
                goodsVO.setProductId(detailsDTO.getProductId() + "");
                bean.setGoodsVO(goodsVO);
                shopCartPaymentConfirmBeans.add(bean);
            }

            OkHttpManager.getInstance().postRequestObject(Api.SHOPCART_PAYMENTCONFIRM, shopCartPaymentConfirmBeans, new BaseCallBack<BaseResult<PaymentConfirmBean>>() {
                @Override
                public void onFailure(Call call, IOException e) {
                    cancelLoading();
                }

                @Override
                public void onSuccess(Call call, Response response, BaseResult<PaymentConfirmBean> result) {
                    commitObject = result.getData();
                    setPrice(commitObject.getTotalPrice(), commitObject.getTotalUseCoupon(), commitObject.getRemainTotalQuota());
                    cancelLoading();
                    //库存不足
                    if (commitObject != null && commitObject.getStockNotEnoughProducts() != null && commitObject.getStockNotEnoughProducts().size() > 0) {
                        boolean select = false;
                        if (details != null) {
                            for (ShopCartBean.DetailsDTO detailsDTO : details) {
                                for (PaymentConfirmBean.ProductsDTO productsDTO : commitObject.getStockNotEnoughProducts()) {
                                    if (detailsDTO.getGoodsId() == productsDTO.getGoodsVO().getGoodsId()) {
                                        //重置库存
                                        detailsDTO.getGoodsVO().setStock(productsDTO.getGoodsVO().getStock());
                                        //库存为0，移除选择
                                        if (productsDTO.getGoodsVO().getStock() == 0) {
                                            detailsDTO.setSelect(false);
                                            select = true;
                                        }
                                    }
                                }
                            }
                        }
                        refreshMultiItemFragment(select, commitObject.getStockNotEnoughProducts());
                        if (payment) {
                            ToastUtils.showToast("存在库存不足的商品，请调整后重新结算购买");
                        }
                    } else {
                        if (payment) {
                            goCommitObject();
                        }
                    }
                }

                @Override
                public void onError(Call call, int statusCode, Exception e) {
                    cancelLoading();
                }
            });
        } else {
            setPrice("0", "0", "0");
        }
    }

}