package com.yiqizhuan.app.ui.category;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.CategoryDefaultBean;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.bean.ProductSearchDefaultBean;
import com.yiqizhuan.app.databinding.FragmentCategoryBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.category.item.FenLeiFlexibleItem;
import com.yiqizhuan.app.ui.category.item.FenLeiGoodsFlexibleItem;
import com.yiqizhuan.app.ui.category.item.JinGangQuCategoryFlexibleItem;
import com.yiqizhuan.app.ui.category.item.JinGangQuCategoryPopFlexibleItem;
import com.yiqizhuan.app.ui.home.item.BottomFlexibleItem;
import com.yiqizhuan.app.ui.home.item.JinGangQuFlexibleItem;
import com.yiqizhuan.app.ui.search.SearchActivity;
import com.yiqizhuan.app.util.AnimationUtil;
import com.yiqizhuan.app.util.GlideUtil;
import com.yiqizhuan.app.util.PhoneUtil;
import com.yiqizhuan.app.util.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author LiPeng
 * @create 2024-07-09 7:33 PM
 */
public class CategoryFragment extends BaseFragment implements View.OnClickListener {
    FragmentCategoryBinding binding;
    private FlexibleAdapter<IFlexible> jinGangWeiFlexibleAdapter;
    private FlexibleAdapter<IFlexible> jinGangWeiPopFlexibleAdapter;

    private FlexibleAdapter<IFlexible> fenLeiFlexibleAdapter;

    private FlexibleAdapter<IFlexible> fenLeiGoodsFlexibleAdapter;

    private int page = 1;
    private int size = 10;
    //为你推荐数据
    private List<ProductSearchDefaultBean> productSearchDefaultBeans;
    //一级分类
    private List<CategoryDefaultBean> categoryDefaultBeanList;
    private CategoryDefaultBean categoryDefaultBeanCurrent;
    //二级分类
    private List<CategoryDefaultBean> category2DefaultBeanList;
    private CategoryDefaultBean category2DefaultBeanCurrent;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder);
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder1);
        StatusBarUtils.setViewHeaderPlaceholder(binding.viewHeaderPlaceholder2);
        initView();
        productSearchDefault();
        return root;
    }

    private void initView() {
        binding.rlySearch.setOnClickListener(this);
        binding.llyZhanKai.setOnClickListener(this);
        binding.rlyShouQi.setOnClickListener(this);
        binding.rlyPop.setOnClickListener(this);
        binding.rlyPopBottom.setOnClickListener(this);
        jinGangWeiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinGangWei.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rcJinGangWei.setAdapter(jinGangWeiFlexibleAdapter);
        binding.rcJinGangWei.setItemAnimator(new DefaultItemAnimator());

        jinGangWeiPopFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcJinGangWeiPop.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false));
        binding.rcJinGangWeiPop.setAdapter(jinGangWeiPopFlexibleAdapter);
        binding.rcJinGangWeiPop.setItemAnimator(new DefaultItemAnimator());

        fenLeiFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcFenLei.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcFenLei.setAdapter(fenLeiFlexibleAdapter);
        binding.rcFenLei.setItemAnimator(new DefaultItemAnimator());

        fenLeiGoodsFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.rcFenLeiGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcFenLeiGoods.setAdapter(fenLeiGoodsFlexibleAdapter);
        binding.rcFenLeiGoods.setItemAnimator(new DefaultItemAnimator());

        binding.smartRefreshLayout.setEnableRefresh(false);
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                productByCategory(category2DefaultBeanCurrent.getId(), false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rlySearch:
                intent = new Intent(getActivity(), SearchActivity.class);
                if (categoryDefaultBeanCurrent != null) {
                    intent.putExtra("firstCategoryId", categoryDefaultBeanCurrent.getId());
                }
                startActivity(intent);
                break;
            case R.id.llyZhanKai:
                binding.rlyPop.setVisibility(View.VISIBLE);
                break;
            case R.id.rlyPopBottom:
            case R.id.rlyShouQi:
                binding.rlyPop.setVisibility(View.GONE);
                break;
            case R.id.rlyPop:
                break;
        }
    }

    private void initCategoryDefaultData() {
        if (categoryDefaultBeanList == null) {
            categoryDefaultBeanList = new ArrayList<>();
        }
        for (int i = 0; i < categoryDefaultBeanList.size(); i++) {
            if (i == 0) {
                categoryDefaultBeanList.get(i).setSelect(true);
                categoryDefaultBeanCurrent = categoryDefaultBeanList.get(i);
            }
            JinGangQuCategoryFlexibleItem jinGangQuCategoryFlexibleItem = new JinGangQuCategoryFlexibleItem(getActivity(), categoryDefaultBeanList.get(i));
            jinGangQuCategoryFlexibleItem.setOnClickListener(new JinGangQuCategoryFlexibleItem.OnClickListener() {
                @Override
                public void onPositiveClick(CategoryDefaultBean categoryDefaultBean, int position) {
                    manageCategory1(categoryDefaultBean, position);
                }
            });
            jinGangWeiFlexibleAdapter.addItem(jinGangQuCategoryFlexibleItem);
            JinGangQuCategoryPopFlexibleItem jinGangQuCategoryPopFlexibleItem = new JinGangQuCategoryPopFlexibleItem(getActivity(), categoryDefaultBeanList.get(i));
            jinGangQuCategoryPopFlexibleItem.setOnClickListener(new JinGangQuCategoryPopFlexibleItem.OnClickListener() {
                @Override
                public void onPositiveClick(CategoryDefaultBean categoryDefaultBean, int position) {
                    manageCategory1(categoryDefaultBean, position);
                    binding.rlyPop.setVisibility(View.GONE);
                }
            });
            jinGangWeiPopFlexibleAdapter.addItem(jinGangQuCategoryPopFlexibleItem);
        }
        initCategorySecond();
    }

    private void initCategorySecond() {
        GlideUtil.loadImage(categoryDefaultBeanCurrent.getBannerUrl(), binding.ivBanner, 8);
        categorySecond(categoryDefaultBeanCurrent.getId());
    }

    private void manageCategory1(CategoryDefaultBean categoryDefaultBean, int position) {
        if (!categoryDefaultBean.isSelect()) {
            for (CategoryDefaultBean categoryDefaultBean1 : categoryDefaultBeanList) {
                if (TextUtils.equals(categoryDefaultBean.getId(), categoryDefaultBean1.getId())) {
                    categoryDefaultBean1.setSelect(true);
                    categoryDefaultBeanCurrent = categoryDefaultBean1;
                } else {
                    categoryDefaultBean1.setSelect(false);
                }
            }
            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcJinGangWei.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(position, 260);
            jinGangWeiFlexibleAdapter.notifyDataSetChanged();
            jinGangWeiPopFlexibleAdapter.notifyDataSetChanged();
            initCategorySecond();
        }
    }

    private void initCategory2() {
        if (fenLeiFlexibleAdapter != null) {
            fenLeiFlexibleAdapter.clear();
        }
        if (category2DefaultBeanList == null) {
            category2DefaultBeanList = new ArrayList<>();
        }
        if (recommendExist() != null) {
            CategoryDefaultBean categoryDefaultBean = new CategoryDefaultBean();
            categoryDefaultBean.setId("-1");
            categoryDefaultBean.setName("为你推荐");
            category2DefaultBeanList.add(0, categoryDefaultBean);
        }
        for (int i = 0; i < category2DefaultBeanList.size(); i++) {
            if (i == 0) {
                category2DefaultBeanList.get(i).setSelect(true);
                category2DefaultBeanCurrent = category2DefaultBeanList.get(i);
            }
            FenLeiFlexibleItem flexibleItem = new FenLeiFlexibleItem(getActivity(), category2DefaultBeanList.get(i));
            flexibleItem.setOnClickListener(new FenLeiFlexibleItem.OnClickListener() {
                @Override
                public void onPositiveClick(int position, CategoryDefaultBean categoryDefaultBean) {
                    if (!categoryDefaultBean.isSelect()) {
                        for (CategoryDefaultBean categoryDefaultBean1 : category2DefaultBeanList) {
                            if (TextUtils.equals(categoryDefaultBean.getId(), categoryDefaultBean1.getId())) {
                                categoryDefaultBean1.setSelect(true);
                                category2DefaultBeanCurrent = categoryDefaultBean1;
                            } else {
                                categoryDefaultBean1.setSelect(false);
                            }
                        }
                        fenLeiFlexibleAdapter.notifyDataSetChanged();
                        initGoods();
                    }
                }
            });
            fenLeiFlexibleAdapter.addItem(flexibleItem);
        }
        initGoods();
    }

    private void initGoods() {
        binding.tvTitle.setText(category2DefaultBeanCurrent.getName());
        if (TextUtils.equals(category2DefaultBeanCurrent.getId(), "-1")) {
            binding.smartRefreshLayout.setEnableLoadMore(false);
            binding.smartRefreshLayout.setNoMoreData(true);
            fenLeiGoodsFlexibleAdapter.clear();
            initCategory2Goods(recommendExist().getProducts());
            fenLeiGoodsFlexibleAdapter.addItem(new BottomFlexibleItem(getActivity()));
        } else {
            initFirstGoods();
        }
    }

    private void initFirstGoods() {
        binding.smartRefreshLayout.setEnableLoadMore(true);
        binding.smartRefreshLayout.setNoMoreData(false);
        page = 1;
        productByCategory(category2DefaultBeanCurrent.getId(), true);
    }

    private ProductSearchDefaultBean recommendExist() {
        if (productSearchDefaultBeans != null && productSearchDefaultBeans.size() > 0) {
            for (ProductSearchDefaultBean productSearchDefaultBean : productSearchDefaultBeans) {
                if (TextUtils.equals(productSearchDefaultBean.getCategoryId() + "", categoryDefaultBeanCurrent.getId()) && productSearchDefaultBean.getProducts() != null && productSearchDefaultBean.getProducts().size() > 0) {
                    return productSearchDefaultBean;
                }
            }
        }
        return null;
    }

    private void initCategory2Goods(List<ProductListBean.Detail> products) {
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                fenLeiGoodsFlexibleAdapter.addItem(new FenLeiGoodsFlexibleItem(getActivity(), products.get(i)));
            }
        }
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
                    categoryDefaultBeanList = new ArrayList<>();
                    for (CategoryDefaultBean categoryDefaultBean : result.getData()) {
                        if (categoryDefaultBean.getHide() != 1) {
                            categoryDefaultBeanList.add(categoryDefaultBean);
                        }
                    }
                    initCategoryDefaultData();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
            }
        });
    }


    /**
     * 获取二级类目列表
     */
    private void categorySecond(String parentId) {
//        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("parentId", parentId);
        OkHttpManager.getInstance().getRequest(Api.category_second, paramsMap, new BaseCallBack<BaseResult<List<CategoryDefaultBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
//                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<CategoryDefaultBean>> result) {
                if (result != null && result.getData() != null) {
                    category2DefaultBeanList = result.getData();
                    initCategory2();
                }
//                cancelLoading();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
//                cancelLoading();
            }
        });
    }

    /**
     * 获取二级分类商品
     *
     * @param categoryId
     */
    private void productByCategory(String categoryId, boolean loading) {
        if (loading) {
            showLoading();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("categoryId", categoryId);
        paramsMap.put("page", page + "");
        paramsMap.put("size", size + "");
        paramsMap.put("child", "1");
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_BY_CATEGORY, paramsMap, new BaseCallBack<ProductListBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (loading) {
                    cancelLoading();
                }
            }

            @Override
            public void onSuccess(Call call, Response response, ProductListBean result) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (page == 1) {
                    fenLeiGoodsFlexibleAdapter.clear();
                }
                if (result != null && result.getData() != null && result.getData().getDetails() != null && result.getData().getDetails().size() > 0) {
                    page++;
                    initCategory2Goods(result.getData().getDetails());
                } else {
                    fenLeiGoodsFlexibleAdapter.addItem(new BottomFlexibleItem(getActivity()));
                    binding.smartRefreshLayout.setEnableLoadMore(false);
                    binding.smartRefreshLayout.setNoMoreData(true);
                }
                if (loading) {
                    cancelLoading();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (loading) {
//                    cancelLoading();
                }
            }
        });
    }

    /**
     * 默认推荐分类
     */
    private void productSearchDefault() {
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().getRequest(Api.PRODUCT_SEARCH_DEFAULT, paramsMap, new BaseCallBack<BaseResult<List<ProductSearchDefaultBean>>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                getCategoryDefault();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<List<ProductSearchDefaultBean>> result) {
                if (result != null && result.getData() != null) {
                    productSearchDefaultBeans = result.getData();
                }
                getCategoryDefault();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                getCategoryDefault();
            }
        });
    }

}


