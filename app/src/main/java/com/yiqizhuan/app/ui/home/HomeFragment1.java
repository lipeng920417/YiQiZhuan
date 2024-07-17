package com.yiqizhuan.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BannerBean;
import com.yiqizhuan.app.bean.ProductDefaultBean;
import com.yiqizhuan.app.databinding.FragmentHome1Binding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.home.adapter.TabFragmentAdapter;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.SkipActivityUtil;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.webview.WebActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class HomeFragment1 extends BaseFragment {

    private FragmentHome1Binding binding;
    private List<BaseFragment> fragments;
    private List<String> title;
    private TabFragmentAdapter tabFragmentAdapter;
    private HomeTabFragment everydayFragment;
    private HomeTabFragment freeFragment;
    private ProductDefaultBean productDefaultBean;
    private Handler mHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHome1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        productDefault();
        initHeaderView();
        return root;
    }

    private void initHeaderView() {
        //精选汇
        binding.llyJingXuanHui.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_LIST_ONE);
            startActivity(intent);
        });
        //共享汇
        binding.llyGongXiangHui.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_SHARED);
            startActivity(intent);
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getCurated_product() != null && productDefaultBean.getData().getCurated_product().size() > 2) {
                binding.ivOne.setImageDrawable(null);
                binding.ivTwo.setImageDrawable(null);
                binding.ivThree.setImageDrawable(null);
                GlideUtil.loadImageCrossFade(productDefaultBean.getData().getCurated_product().get(2).getMainImage(), binding.ivOne, 6);
                GlideUtil.loadImageCrossFade(productDefaultBean.getData().getCurated_product().get(1).getMainImage(), binding.ivTwo, 6);
                GlideUtil.loadImageCrossFade(productDefaultBean.getData().getCurated_product().get(0).getMainImage(), binding.ivThree, 6);
            }
            mHandler.postDelayed(this, 3000);
        }
    };


    private void initHeaderData() {
        //精选汇
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getCurated_product() != null && productDefaultBean.getData().getCurated_product().size() > 2) {
            GlideUtil.loadImage(productDefaultBean.getData().getCurated_product().get(0).getMainImage(), binding.ivOne, 6);
            GlideUtil.loadImage(productDefaultBean.getData().getCurated_product().get(1).getMainImage(), binding.ivTwo, 6);
            GlideUtil.loadImage(productDefaultBean.getData().getCurated_product().get(2).getMainImage(), binding.ivThree, 6);
            mHandler.removeCallbacks(runnable); //取消执行
            mHandler.postDelayed(runnable, 3000); //延时执行
            binding.ivOne.setOnClickListener(view -> {
//                Intent broker = new Intent(getActivity(), WebActivity.class);
//                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + productDefaultBean.getData().getCurated_product().get(0).getProductId() + "&type=1");
//                startActivity(broker);
                SkipActivityUtil.goGoodsDetail(getActivity(), productDefaultBean.getData().getCurated_product().get(0).getProductId() , "1");
            });
            binding.ivTwo.setOnClickListener(view -> {
//                Intent broker = new Intent(getActivity(), WebActivity.class);
//                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + productDefaultBean.getData().getCurated_product().get(1).getProductId() + "&type=1");
//                startActivity(broker);
                SkipActivityUtil.goGoodsDetail(getActivity(), productDefaultBean.getData().getCurated_product().get(1).getProductId() , "1");
            });
            binding.ivThree.setOnClickListener(view -> {
//                Intent broker = new Intent(getActivity(), WebActivity.class);
//                broker.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_GOODS + "?productId=" + productDefaultBean.getData().getCurated_product().get(2).getProductId() + "&type=1");
//                startActivity(broker);
                SkipActivityUtil.goGoodsDetail(getActivity(), productDefaultBean.getData().getCurated_product().get(2).getProductId() , "1");
            });
        }
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getEarn_together() != null && productDefaultBean.getData().getEarn_together().size() > 1) {
            ArrayList<BannerBean> picUrls = new ArrayList<>();
            for (int i = 0; i < productDefaultBean.getData().getEarn_together().size(); i = i + 2) {
                if (i + 1 < productDefaultBean.getData().getEarn_together().size()) {
                    BannerBean bean = new BannerBean(productDefaultBean.getData().getEarn_together().get(i).getMainImage(), productDefaultBean.getData().getEarn_together().get(i).getProductId(), productDefaultBean.getData().getEarn_together().get(i + 1).getMainImage(), productDefaultBean.getData().getEarn_together().get(i + 1).getProductId());
                    picUrls.add(bean);
                }
            }
            //共享汇
            binding.commonBanner.initData(picUrls, getActivity());
        }
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();
        title = new ArrayList<>();
        title.add("悦享汇");
        title.add("畅购汇");
        Bundle bundleEveryday = new Bundle();
        bundleEveryday.putString("type", "3");
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getDaily_exchange() != null && productDefaultBean.getData().getDaily_exchange().size() > 0) {
            bundleEveryday.putSerializable("productListBean", productDefaultBean.getData().getDaily_exchange().get(0));
        }
        everydayFragment = new HomeTabFragment();
        everydayFragment.setArguments(bundleEveryday);
        fragments.add(everydayFragment);
        freeFragment = new HomeTabFragment();
        Bundle bundleFree = new Bundle();
        bundleFree.putString("type", "4");
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getEnjoyable_exchange() != null && productDefaultBean.getData().getEnjoyable_exchange().size() > 0) {
            bundleFree.putSerializable("productListBean", productDefaultBean.getData().getEnjoyable_exchange().get(0));
        }
        freeFragment.setArguments(bundleFree);
        fragments.add(freeFragment);
        tabFragmentAdapter = new TabFragmentAdapter(getChildFragmentManager(), fragments, title, getContext());
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
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    private void setTabStyle(View view, boolean b) {
        if (view == null) {
            return;
        }
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        RelativeLayout rly = view.findViewById(R.id.rly);
        if (b) {
            tvTitle.setTextColor(getResources().getColor(R.color.color_FFF3D4));
            binding.tab.setBackground(getResources().getDrawable(R.mipmap.ic_tab_bg_right));
        } else {
            tvTitle.setTextColor(getResources().getColor(R.color.color_222222));
            binding.tab.setBackground(getResources().getDrawable(R.mipmap.ic_tab_bg_left));
        }

        if (b) {
            if ("悦享汇".equals(tvTitle.getText().toString())) {
                binding.tab.setBackground(getResources().getDrawable(R.mipmap.ic_tab_bg_right));
            } else {
                binding.tab.setBackground(getResources().getDrawable(R.mipmap.ic_tab_bg_left));
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void productDefault() {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_DEFAULT, paramsMap, new BaseCallBack<ProductDefaultBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
                initHeaderData();
            }

            @Override
            public void onSuccess(Call call, Response response, ProductDefaultBean result) {
                cancelLoading();
                productDefaultBean = result;
                initHeaderData();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
                initHeaderData();
            }
        });
    }


}