package com.yiqizhuan.app.ui.remit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.databinding.FragmentRemitBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.home.item.BottomFlexibleItem;
import com.yiqizhuan.app.ui.remit.adapter.BannerAdapter;
import com.yiqizhuan.app.ui.remit.item.ChaoZhiHaoWuFlexibleItem;
import com.yiqizhuan.app.ui.remit.item.HaoWuTuiJianFlexibleItem;
import com.yiqizhuan.app.ui.shopping.ShoppingViewModel;
import com.yiqizhuan.app.util.PhoneUtil;
import com.yiqizhuan.app.util.SizeUtils;
import com.yiqizhuan.app.util.StatusBarUtils;
import com.yiqizhuan.app.views.ScaleInTransformerCustom;
import com.yiqizhuan.app.views.ScaleInTransformerCustom1;
import com.zhpan.bannerview.constants.PageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;

public class RemitFragment extends BaseFragment implements View.OnClickListener {

    private FragmentRemitBinding binding;
    private FlexibleAdapter<IFlexible> flexibleAdapter;
    private FlexibleAdapter<IFlexible> haoWuFlexibleAdapter;
    private int page = 1;
    private int size = 10;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentRemitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        initView();
        curatedCarousel();
        curatedMiddle();
        productList();
        return root;
    }

    private void initView() {
        binding.bannerViewPager.setAdapter(new BannerAdapter(getActivity()))
                .setAutoPlay(true)
                .setScrollDuration(600).setInterval(4000)
                .setIndicatorSliderColor(getActivity().getColor(R.color.color_transparent),getActivity().getColor(R.color.color_transparent))
                .setPageMargin(SizeUtils.dp2px(8))
                .setRevealWidth(SizeUtils.dp2px(0), (int) (SizeUtils.getScreenWidth()*0.69))
                .setPageStyle(PageStyle.MULTI_PAGE_SCALE, 0.85f)
                .setPageTransformer(new ScaleInTransformerCustom(0.85f))
                .create();
        binding.ivXiaoxi.setOnClickListener(this);
        binding.smartRefreshLayout.setEnableRefresh(false);
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                productList();
            }
        });
        //设置 Footer 为 球脉冲 样式
        binding.smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        haoWuFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcHaoWu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rcHaoWu.setAdapter(haoWuFlexibleAdapter);
        binding.rcHaoWu.setItemAnimator(new DefaultItemAnimator());
        flexibleAdapter = new FlexibleAdapter<>(null);
        binding.rc.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        binding.rc.setAdapter(flexibleAdapter);
        binding.rc.setItemAnimator(new DefaultItemAnimator());
    }


    private void updateCuratedCarouselUI(List<ProductListBean.Detail> result) {
        binding.bannerViewPager.refreshData(result);
    }

    private void updateCuratedMiddleUI(List<ProductListBean.Detail> result) {
        for (ProductListBean.Detail detail : result) {
            haoWuFlexibleAdapter.addItem(new HaoWuTuiJianFlexibleItem(getActivity(), detail));
        }
    }

    private void updateDataUI(List<ProductListBean.Detail> result) {
        for (ProductListBean.Detail detail : result) {
            flexibleAdapter.addItem(new ChaoZhiHaoWuFlexibleItem(getActivity(), detail));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }

    private void curatedCarousel() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.CURATED_CAROUSEL, paramsMap, new BaseCallBack<BaseResult<List<ProductListBean.Detail>>>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<ProductListBean.Detail>> result) {
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    updateCuratedCarouselUI(result.getData());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }
        });
    }


    private void curatedMiddle() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.CURATED_MIDDLE, paramsMap, new BaseCallBack<BaseResult<List<ProductListBean.Detail>>>() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<ProductListBean.Detail>> result) {
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    updateCuratedMiddleUI(result.getData());
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {

            }
        });
    }


    /**
     * 查询商品列表
     */
    private void productList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", "1");
        paramsMap.put("page", page + "");
        paramsMap.put("size", size + "");
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_LIST, paramsMap, new BaseCallBack<ProductListBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(Call call, Response response, ProductListBean result) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }

                if (result != null && result.getData() != null && result.getData().getDetails() != null && result.getData().getDetails().size() > 0) {
                    page++;
                    updateDataUI(result.getData().getDetails());
                } else {
                    flexibleAdapter.addItem(new BottomFlexibleItem(getActivity()));
                    binding.smartRefreshLayout.setEnableLoadMore(false);
                    binding.smartRefreshLayout.setNoMoreData(true);
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ivXiaoxi:
                PhoneUtil.getPhone(getActivity());
                break;
        }
    }
}