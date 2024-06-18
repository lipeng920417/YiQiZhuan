package com.yiqizhuan.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.CategoryDefaultBean;
import com.yiqizhuan.app.bean.GetDiscountCategoryBean;
import com.yiqizhuan.app.bean.ProductDefaultBean;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.databinding.FragmentHomeBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.home.adapter.BannerZhuanQuAdapter;
import com.yiqizhuan.app.ui.home.item.JinGangQuFlexibleItem;
import com.yiqizhuan.app.ui.home.item.JinRiFlexibleItem;
import com.yiqizhuan.app.util.BigDecimalUtil;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.PhoneUtil;
import com.yiqizhuan.app.util.SizeUtils;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;
import com.yiqizhuan.app.webview.WebActivity;
import com.zhpan.bannerview.constants.PageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private FlexibleAdapter<IFlexible> jinGangWeiFlexibleAdapter;
    private FlexibleAdapter<IFlexible> jinRiFlexibleAdapter;
    private ProductDefaultBean productDefaultBean;
    private GetDiscountCategoryBean discountCategoryBean;
    private List<CategoryDefaultBean> categoryDefaultBeanList;
    private int page = 1;
    private int size = 50;
    private CountDownTimer countDownTimer;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        initView();
        getDiscountCategory();
        productDefault();
//        productBestsellers();
        productCrazyBuy();
        return root;
    }

    private void initView() {
        binding.ivXiaoxi.setOnClickListener(this);
        binding.rlySearch.setOnClickListener(this);
        binding.vGongXiang.setOnClickListener(this);
        binding.vChangXiang.setOnClickListener(this);
        binding.vYuexiang.setOnClickListener(this);
        binding.llyZhuanQu.setOnClickListener(this);
        jinGangWeiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinGangWei.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false));
        binding.rcJinGangWei.setAdapter(jinGangWeiFlexibleAdapter);
        binding.rcJinGangWei.setItemAnimator(new DefaultItemAnimator());
        //尊享专区
        List<Integer> strings = new ArrayList<>();
        strings.add(R.mipmap.ic_zunxiang1);
        strings.add(R.mipmap.ic_zunxiang2);
        strings.add(R.mipmap.ic_zunxiang3);
        strings.add(R.mipmap.ic_zunxiang4);
        strings.add(R.mipmap.ic_zunxiang5);
        strings.add(R.mipmap.ic_zunxiang6);
        strings.add(R.mipmap.ic_zunxiang7);
        strings.add(R.mipmap.ic_zunxiang8);
        binding.bannerViewPager.setAdapter(new BannerZhuanQuAdapter(getActivity()))
                .setAutoPlay(true)
                .setScrollDuration(600).setInterval(4500)
                .setIndicatorSliderColor(getActivity().getColor(R.color.color_transparent), getActivity()
                        .getColor(R.color.color_transparent))
                .setPageMargin(SizeUtils.dp2px(8))
                .setRevealWidth(SizeUtils.dp2px(0), (int) (SizeUtils.getScreenWidth()*0.72))
                .setPageStyle(PageStyle.NORMAL)
                .create(strings);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.bannerViewPager.refreshData(strings);
//            }
//        },500);

        jinRiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinRi.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcJinRi.setAdapter(jinRiFlexibleAdapter);
        binding.rcJinRi.setItemAnimator(new DefaultItemAnimator());

        countDownTimer();
    }

    private void initCategoryDefaultData() {
        if (categoryDefaultBeanList == null) {
            categoryDefaultBeanList = new ArrayList<>();
        }
        categoryDefaultBeanList.add(new CategoryDefaultBean("-1", "积分兑换", "", ""));
        for (CategoryDefaultBean categoryDefaultBean : categoryDefaultBeanList) {
            jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), categoryDefaultBean, discountCategoryBean));
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ivXiaoxi:
                PhoneUtil.getPhone(getActivity());
                break;
            case R.id.rlySearch:
                DialogUtil.build1BtnDialog(getActivity(), "搜索模块正在开发中，敬请期待", "我知道了", true, new DialogUtil.DialogListener1Btn() {
                    @Override
                    public void onPositiveClick(View v) {

                    }
                }).show();
                break;
            //悦享汇
            case R.id.vYuexiang:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.web_list_three);
                startActivity(intent);
                break;
            //畅享汇
            case R.id.vChangXiang:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_LIST_FOUR);
                startActivity(intent);
                break;
            //共享汇
            case R.id.vGongXiang:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_SHARED);
                startActivity(intent);
                break;
            case R.id.llyZhuanQu:
                LiveEventBus.get("goZunXiangHui").post("");
                break;
        }
    }

    private void countDownTimer() {
        // 获取今天结束的时间（23:59:59）对应的毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long todayEndMillis = calendar.getTimeInMillis();
        long currentMillis = new Date().getTime();
        // 计算剩余时间
        long remainingMillis = todayEndMillis - currentMillis;
        countDownTimer = new CountDownTimer(remainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / (60 * 60 * 1000);
                long minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000);
                long seconds = (millisUntilFinished % (60 * 1000)) / 1000;
                if (hours < 10) {
                    binding.tvHour.setText("0" + hours + "");
                    binding.tvHour1.setText("0" + hours + "");
                } else {
                    binding.tvHour.setText(hours + "");
                    binding.tvHour1.setText(hours + "");
                }
                if (minutes < 10) {
                    binding.tvMinute.setText("0" + minutes + "");
                    binding.tvMinute1.setText("0" + minutes + "");
                } else {
                    binding.tvMinute.setText(minutes + "");
                    binding.tvMinute1.setText(minutes + "");
                }
                if (seconds < 10) {
                    binding.tvSecond.setText("0" + seconds + "");
                    binding.tvSecond1.setText("0" + seconds + "");
                } else {
                    binding.tvSecond.setText(seconds + "");
                    binding.tvSecond1.setText(seconds + "");
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }


    private void initDefaultData() {
        //悦享
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getDaily_exchange() != null && productDefaultBean.getData().getDaily_exchange().size() > 1) {
            GlideUtil.loadImage(productDefaultBean.getData().getDaily_exchange().get(0).getMainImage(), binding.ivYueXiang1);
            binding.ivYueXiang1.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getDaily_exchange().get(0).getProductId(),"3"));
            binding.tvYueXiangPrice1.setText("￥" + productDefaultBean.getData().getDaily_exchange().get(0).getOriginalPrice());
            GlideUtil.loadImage(productDefaultBean.getData().getDaily_exchange().get(1).getMainImage(), binding.ivYueXiang2);
            binding.ivYueXiang2.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getDaily_exchange().get(1).getProductId(),"3"));
            binding.tvYueXiangPrice2.setText("￥" + productDefaultBean.getData().getDaily_exchange().get(1).getOriginalPrice());
        }
        //畅享
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getEnjoyable_exchange() != null && productDefaultBean.getData().getEnjoyable_exchange().size() > 1) {
            GlideUtil.loadImage(productDefaultBean.getData().getEnjoyable_exchange().get(0).getMainImage(), binding.ivChangXiang1);
            binding.ivChangXiang1.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getEnjoyable_exchange().get(0).getProductId(),"4"));
            GlideUtil.loadImage(productDefaultBean.getData().getEnjoyable_exchange().get(1).getMainImage(), binding.ivChangXiang2);
            binding.ivChangXiang2.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getEnjoyable_exchange().get(1).getProductId(),"4"));
        }
        //共享
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getEarn_together() != null && productDefaultBean.getData().getEarn_together().size() > 3) {
            GlideUtil.loadImage(productDefaultBean.getData().getEarn_together().get(0).getMainImage(), binding.ivGongXiang1);
            binding.ivGongXiang1.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getEarn_together().get(0).getProductId(),"2"));
            binding.tvGongXiangPriceB1.setText("￥" + productDefaultBean.getData().getEarn_together().get(0).getOriginalPrice());
            String price = "已补" + BigDecimalUtil.round(Double.valueOf(productDefaultBean.getData().getEarn_together().get(0).getOriginalPrice()) * 0.16, 1) + "元";
            SpannableString spannableString = new SpannableString(price);
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.3f);
            spannableString.setSpan(relativeSizeSpan, 2, price.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvGongXiangPrice1.setText(spannableString);

            GlideUtil.loadImage(productDefaultBean.getData().getEarn_together().get(1).getMainImage(), binding.ivGongXiang2);
            binding.ivGongXiang2.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getEarn_together().get(1).getProductId(),"2"));
            binding.tvGongXiangPriceB2.setText("￥" + productDefaultBean.getData().getEarn_together().get(1).getOriginalPrice());
            String price1 = "已补" + BigDecimalUtil.round(Double.valueOf(productDefaultBean.getData().getEarn_together().get(1).getOriginalPrice()) * 0.16, 1) + "元";
            SpannableString spannableString1 = new SpannableString(price1);
            RelativeSizeSpan relativeSizeSpan1 = new RelativeSizeSpan(1.3f);
            spannableString1.setSpan(relativeSizeSpan1, 2, price1.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvGongXiangPrice2.setText(spannableString1);

            GlideUtil.loadImage(productDefaultBean.getData().getEarn_together().get(2).getMainImage(), binding.ivGongXiang3);
            binding.ivGongXiang3.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getEarn_together().get(2).getProductId(),"2"));
            binding.tvGongXiangPriceB3.setText("￥" + productDefaultBean.getData().getEarn_together().get(2).getOriginalPrice());
            String price2 = "已补" + BigDecimalUtil.round(Double.valueOf(productDefaultBean.getData().getEarn_together().get(2).getOriginalPrice()) * 0.16, 1) + "元";
            SpannableString spannableString2 = new SpannableString(price2);
            RelativeSizeSpan relativeSizeSpan2 = new RelativeSizeSpan(1.3f);
            spannableString2.setSpan(relativeSizeSpan2, 2, price2.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvGongXiangPrice3.setText(spannableString2);

            GlideUtil.loadImage(productDefaultBean.getData().getEarn_together().get(3).getMainImage(), binding.ivGongXiang4);
            binding.ivGongXiang4.setOnClickListener(view -> skipWebView(productDefaultBean.getData().getEarn_together().get(3).getProductId(),"2"));
            binding.tvGongXiangPriceB4.setText("￥" + productDefaultBean.getData().getEarn_together().get(3).getOriginalPrice());
            binding.tvGongXiangPrice4.setText("已补" + BigDecimalUtil.round(Double.valueOf(productDefaultBean.getData().getEarn_together().get(3).getOriginalPrice()) * 0.16, 1) + "元");
            String price3 = "已补" + BigDecimalUtil.round(Double.valueOf(productDefaultBean.getData().getEarn_together().get(3).getOriginalPrice()) * 0.16, 1) + "元";
            SpannableString spannableString3 = new SpannableString(price3);
            RelativeSizeSpan relativeSizeSpan3 = new RelativeSizeSpan(1.3f);
            spannableString3.setSpan(relativeSizeSpan3, 2, price3.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvGongXiangPrice4.setText(spannableString3);
        }
    }

    private void skipWebView(String productId,String type){
        Intent broker = new Intent(getActivity(), WebActivity.class);
        broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + productId + "&type="+type);
        getActivity().startActivity(broker);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    /**
     * 获取 类目立减 图标
     */
    private void getDiscountCategory() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.CATEGORY_GETDISCOUNTCATEGORY, paramsMap, new BaseCallBack<GetDiscountCategoryBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCategoryDefault();
            }

            @Override
            public void onSuccess(Call call, Response response, GetDiscountCategoryBean getDiscountCategoryBean) {
                discountCategoryBean = getDiscountCategoryBean;
                getCategoryDefault();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                getCategoryDefault();
            }
        });
    }

    /**
     * 获取 类目列表
     */
    private void getCategoryDefault() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.CATEGORY_DEFAULT, paramsMap, new BaseCallBack<BaseResult<List<CategoryDefaultBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<CategoryDefaultBean>> result) {
                if (result != null && result.getData() != null) {
                    categoryDefaultBeanList = result.getData();
                    initCategoryDefaultData();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }


    /**
     * 默认商品
     */
    private void productDefault() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_DEFAULT, paramsMap, new BaseCallBack<ProductDefaultBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
                initDefaultData();
            }

            @Override
            public void onSuccess(Call call, Response response, ProductDefaultBean result) {
                cancelLoading();
                productDefaultBean = result;
                initDefaultData();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
                initDefaultData();
            }
        });
    }

    /**
     * 本周热销
     */
//    private void productBestsellers() {
//        HashMap<String, String> paramsMap = new HashMap<>();
//        OkHttpManager.getInstance().getRequest(Api.PRODUCT_BESTSELLERS, paramsMap, new BaseCallBack<BaseResult<List<ProductListBean.Detail>>>() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onSuccess(Call call, Response response, BaseResult<List<ProductListBean.Detail>> result) {
//                if (result != null && result.getData() != null && result.getData().size() > 3) {
//                    List<List<ProductListBean.Detail>> groupedLists = new ArrayList<>();
//                    for (int i = 0; i < result.getData().size(); i += 4) {
//                        List<ProductListBean.Detail> details = new ArrayList<>(result.getData().subList(i, Math.min(i + 4, result.getData().size())));
//                        if (details.size() > 3) {
//                            groupedLists.add(details);
//                        }
//                    }
//                    binding.common4Banner.initData(groupedLists, getActivity());
//                }
//            }
//
//            @Override
//            public void onError(Call call, int statusCode, Exception e) {
//            }
//        });
//    }


    /**
     * 今日畅销
     */
    private void productCrazyBuy() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_CRAZY_BUY, paramsMap, new BaseCallBack<BaseResult<List<ProductListBean.Detail>>>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<ProductListBean.Detail>> result) {
                for (int i = 0; i < result.getData().size(); i++) {
                    jinRiFlexibleAdapter.addItem(new JinRiFlexibleItem(getActivity(), result.getData().get(i)));
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }
        });
    }

}