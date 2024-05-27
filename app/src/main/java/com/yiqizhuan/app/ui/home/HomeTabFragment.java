package com.yiqizhuan.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.databinding.FragmentHomeTabBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.home.item.BottomFlexibleItem;
import com.yiqizhuan.app.ui.home.item.HeaderFlexibleItem;
import com.yiqizhuan.app.ui.home.item.ShopBottomFlexibleItem;
import com.yiqizhuan.app.ui.shopping.ShoppingViewModel;

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
 * @create 2024-04-17 7:12 PM
 */
public class HomeTabFragment extends BaseFragment {

    private FragmentHomeTabBinding binding;
    private FlexibleAdapter<IFlexible> mFlexibleAdapter;
    private String type;
    private ProductListBean.Detail detail;
    private int page = 1;
    private int size = 50;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentHomeTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (getArguments() != null) {
            type = getArguments().getString("type");
            if (getArguments().getSerializable("productListBean") != null) {
                detail = (ProductListBean.Detail) getArguments().getSerializable("productListBean");
            }
        }
        initView();
        productList();
        return root;
    }

    private void initView() {
        binding.smartRefreshLayout.setEnableRefresh(false);
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                productList();
            }
        });
        mFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(mFlexibleAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFlexibleAdapter.addItem(new HeaderFlexibleItem(getActivity(), detail, type));
    }

    private void updateDataUI(ProductListBean result) {
        if (result != null && result.getData() != null && result.getData().getDetails() != null && result.getData().getDetails().size() > 0) {
            if (mFlexibleAdapter.getItemCount() > 1) {
                ShopBottomFlexibleItem flexibleItem = (ShopBottomFlexibleItem) mFlexibleAdapter.getItem(mFlexibleAdapter.getItemCount() - 1);
                if (flexibleItem.detail.size() < 4) {
                    mFlexibleAdapter.removeItem(mFlexibleAdapter.getItemCount() - 1);
                    List<ProductListBean.Detail> details = new ArrayList<>();
                    details.addAll(flexibleItem.detail);
                    int temp = 4 - flexibleItem.detail.size();
                    if (temp == 1) {
                        details.add(result.getData().getDetails().get(0));
                        result.getData().getDetails().remove(0);
                    } else if (temp == 2) {
                        if (result.getData().getDetails().size() >= 2) {
                            details.add(result.getData().getDetails().get(0));
                            details.add(result.getData().getDetails().get(1));
                            result.getData().getDetails().remove(0);
                            result.getData().getDetails().remove(0);
                        } else if (result.getData().getDetails().size() >= 1) {
                            details.add(result.getData().getDetails().get(0));
                            result.getData().getDetails().remove(0);
                        }
                    } else if (temp == 3) {
                        if (result.getData().getDetails().size() >= 3) {
                            details.add(result.getData().getDetails().get(0));
                            details.add(result.getData().getDetails().get(1));
                            details.add(result.getData().getDetails().get(2));
                            result.getData().getDetails().remove(0);
                            result.getData().getDetails().remove(0);
                            result.getData().getDetails().remove(0);
                        } else if (result.getData().getDetails().size() >= 2) {
                            details.add(result.getData().getDetails().get(0));
                            details.add(result.getData().getDetails().get(1));
                            result.getData().getDetails().remove(0);
                            result.getData().getDetails().remove(0);
                        } else if (result.getData().getDetails().size() >= 1) {
                            details.add(result.getData().getDetails().get(0));
                            result.getData().getDetails().remove(0);
                        }
                    }
                    mFlexibleAdapter.addItem(new ShopBottomFlexibleItem(getActivity(), details, type));
                }
            }

            for (int i = 0; i < result.getData().getDetails().size(); i = i + 4) {
                List<ProductListBean.Detail> details = new ArrayList<>();
                details.add(result.getData().getDetails().get(i));
                if (i + 1 < result.getData().getDetails().size()) {
                    details.add(result.getData().getDetails().get(i + 1));
                }
                if (i + 2 < result.getData().getDetails().size()) {
                    details.add(result.getData().getDetails().get(i + 2));
                }
                if (i + 3 < result.getData().getDetails().size()) {
                    details.add(result.getData().getDetails().get(i + 3));
                }
                mFlexibleAdapter.addItem(new ShopBottomFlexibleItem(getActivity(), details, type));
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 查询商品列表
     */
    private void productList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", type);
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
                    updateDataUI(result);
                } else {
                    mFlexibleAdapter.addItem(new BottomFlexibleItem(getActivity()));
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

}
