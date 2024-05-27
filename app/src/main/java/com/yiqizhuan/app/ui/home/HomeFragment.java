package com.yiqizhuan.app.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hjq.permissions.XXPermissions;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.ProductDefaultBean;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.databinding.FragmentHomeBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.home.item.ChangXiangHuiFlexibleItem;
import com.yiqizhuan.app.ui.home.item.JinGangQuFlexibleItem;
import com.yiqizhuan.app.ui.home.item.YueXiangHuiFlexibleItem;
import com.yiqizhuan.app.util.CheckPermission;
import com.yiqizhuan.app.util.PhoneUtil;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.views.dialog.DialogUtil;
import com.yiqizhuan.app.webview.WebActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private FlexibleAdapter<IFlexible> jinGangWeiFlexibleAdapter;
    private FlexibleAdapter<IFlexible> yueXiangHuiFlexibleAdapter;
    private FlexibleAdapter<IFlexible> changXiangHuiFlexibleAdapter;
    private ProductDefaultBean productDefaultBean;
    private int page = 1;
    private int size = 50;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        initView();
        initData();
        productDefault();
        productBestsellers();
        yueList();
        changList();
        return root;
    }

    private void initView() {
        binding.ivXiaoxi.setOnClickListener(this);
        binding.rlySearch.setOnClickListener(this);
        binding.ivBanner1.setOnClickListener(this);
        jinGangWeiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinGangWei.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false));
        binding.rcJinGangWei.setAdapter(jinGangWeiFlexibleAdapter);
        binding.rcJinGangWei.setItemAnimator(new DefaultItemAnimator());
        //尊享汇
        binding.vZunXiangHui.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_LIST_ONE);
            startActivity(intent);
        });
        //共享汇
        binding.vGongXianghui.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_SHARED);
            startActivity(intent);
        });

        yueXiangHuiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcYueXiangHui.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        binding.rcYueXiangHui.setAdapter(yueXiangHuiFlexibleAdapter);
        binding.rcYueXiangHui.setItemAnimator(new DefaultItemAnimator());

        changXiangHuiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcChangXiangHui.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        binding.rcChangXiangHui.setAdapter(changXiangHuiFlexibleAdapter);
        binding.rcChangXiangHui.setItemAnimator(new DefaultItemAnimator());

        //悦享汇
        binding.tvYue.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.web_list_three);
            startActivity(intent);
        });
        //畅享汇
        binding.tvChang.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", BuildConfig.BASE_WEB_URL + WebApi.WEB_LIST_FOUR);
            startActivity(intent);
        });
    }

    private void initData() {
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_daxiaojiadian, "大小家电", "5"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_chayejiushui, "茶叶酒水", "23"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_jaijujiafang, "家具家纺", "13"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_pijuxiangbao, "皮具箱包", "23"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_meizhuanghufu, "美妆护肤", "19"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_mingpaifuzhuang, "名牌服装", "32"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_chufangyongpin, "厨房用品", "8"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_shoubiaoshechi, "手表奢侈", "15"));
        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_richangbaihuo, "日常百货", "1"));
//        jinGangWeiFlexibleAdapter.addItem(new JinGangQuFlexibleItem(getActivity(), R.mipmap.ic_jifenduihuan, "积分兑换", "-1"));
    }

    @Override
    public void onClick(View view) {
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
            case R.id.ivBanner1:
                break;
        }
    }


    private void initDefaultData() {
        //精选汇
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getCurated_product() != null && productDefaultBean.getData().getCurated_product().size() > 0) {
            binding.common1Banner1.initData(productDefaultBean.getData().getCurated_product(), getActivity(), "1");
        }
        if (productDefaultBean != null && productDefaultBean.getData() != null && productDefaultBean.getData().getEarn_together() != null && productDefaultBean.getData().getEarn_together().size() > 0) {
            binding.common1Banner2.initData(productDefaultBean.getData().getEarn_together(), getActivity(), "2");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
    private void productBestsellers() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_BESTSELLERS, paramsMap, new BaseCallBack<BaseResult<List<ProductListBean.Detail>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<ProductListBean.Detail>> result) {
                if (result != null && result.getData() != null && result.getData().size() > 3) {
                    List<List<ProductListBean.Detail>> groupedLists = new ArrayList<>();
                    for (int i = 0; i < result.getData().size(); i += 4) {
                        List<ProductListBean.Detail> details = new ArrayList<>(result.getData().subList(i, Math.min(i + 4, result.getData().size())));
                        if (details.size() > 3) {
                            groupedLists.add(details);
                        }
                    }
                    binding.common4Banner.initData(groupedLists, getActivity());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }

    /**
     * 悦享会
     */
    private void yueList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", "3");
        paramsMap.put("page", page + "");
        paramsMap.put("size", size + "");
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_LIST, paramsMap, new BaseCallBack<ProductListBean>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, ProductListBean result) {
                if (result != null && result.getData() != null && result.getData().getDetails() != null && result.getData().getDetails().size() > 0) {
                    for (int i = 0; i < result.getData().getDetails().size(); i++) {
                        if (i < 6) {
                            yueXiangHuiFlexibleAdapter.addItem(new YueXiangHuiFlexibleItem(getActivity(), result.getData().getDetails().get(i)));
                        }
                    }
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }
        });
    }

    /**
     * 畅享会
     */
    private void changList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", "4");
        paramsMap.put("page", page + "");
        paramsMap.put("size", size + "");
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_LIST, paramsMap, new BaseCallBack<ProductListBean>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, ProductListBean result) {
                for (int i = 0; i < result.getData().getDetails().size(); i++) {
                    if (i < 6) {
                        changXiangHuiFlexibleAdapter.addItem(new ChangXiangHuiFlexibleItem(getActivity(), result.getData().getDetails().get(i)));
                    }
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }
        });
    }

}