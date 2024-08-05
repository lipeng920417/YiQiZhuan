package com.yiqizhuan.app.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.yiqizhuan.app.BuildConfig;
import com.yiqizhuan.app.MainActivity;
import com.yiqizhuan.app.bean.BaseResult;
import com.yiqizhuan.app.bean.OrderListBean;
import com.yiqizhuan.app.bean.ShopcartActionPara;
import com.yiqizhuan.app.databinding.FragmentOrderTabBinding;
import com.yiqizhuan.app.net.Api;
import com.yiqizhuan.app.net.BaseCallBack;
import com.yiqizhuan.app.net.OkHttpManager;
import com.yiqizhuan.app.net.WebApi;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.detail.GoodsDetailActivity;
import com.yiqizhuan.app.ui.home.item.BottomFlexibleItem;
import com.yiqizhuan.app.ui.home.item.NoDataFlexibleItem;
import com.yiqizhuan.app.ui.order.item.OrderFlexibleItem;
import com.yiqizhuan.app.ui.order.view.OrderGoodsView;
import com.yiqizhuan.app.ui.order.view.OrderWuLiuView;
import com.yiqizhuan.app.ui.pay.PayActivity;
import com.yiqizhuan.app.ui.shopping.ShoppingViewModel;
import com.yiqizhuan.app.util.ToastUtils;
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

/**
 * @author LiPeng
 * @create 2024-04-17 7:12 PM
 */
public class OrderTabFragment extends BaseFragment {

    private FragmentOrderTabBinding binding;
    private FlexibleAdapter<IFlexible> mFlexibleAdapter;
    private int page = 1;
    private int size = 20;
    private String type;
    private boolean isVisibleToUser;
    private boolean isFirstLoading;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentOrderTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
        initView();
        return root;
    }

    private void initView() {
        binding.smartRefreshLayout.setEnableRefresh(false);
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                orderList(false);
            }
        });
        //设置 Footer 为 球脉冲 样式
        binding.smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        mFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(mFlexibleAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        isFirstLoading = true;
        if (isVisibleToUser) {
            refresh();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && isFirstLoading) {
            refresh();
        }
    }

    private void refresh() {
        page = 1;
        orderList(true);
    }

    public void updateDataUI(List<OrderListBean.OrdersDTO> ordersDTOS) {
        if (ordersDTOS != null && ordersDTOS.size() > 0) {
            for (OrderListBean.OrdersDTO ordersDTO : ordersDTOS) {
                OrderFlexibleItem orderFlexibleItem = new OrderFlexibleItem(getActivity(), ordersDTO);
                orderFlexibleItem.setOrderListenerSure(new OrderFlexibleItem.OrderListenerSure() {
                    @Override
                    public void delete(OrderListBean.OrdersDTO ordersDTO, int pos) {
                        DialogUtil.build2BtnDialog(getActivity(), "确认删除该订单？", "删除", "取消", true, new DialogUtil.DialogListener2Btn() {
                            @Override
                            public void onPositiveClick(View v) {
                                orderDelete(ordersDTO.getId() + "");
                            }

                            @Override
                            public void onNegativeClick(View v) {

                            }
                        }).show();
                    }

                    @Override
                    public void zaiCiGouMai(OrderListBean.OrdersDTO ordersDTO, int pos) {
                        addShopcartAction(ordersDTO);
                    }

                    @Override
                    public void cancel(OrderListBean.OrdersDTO ordersDTO, int pos) {
                        DialogUtil.build2BtnDialog(getActivity(), "您确认要取消当前订单吗？", "确认取消", "暂不取消", true, new DialogUtil.DialogListener2Btn() {
                            @Override
                            public void onPositiveClick(View v) {
                                orderCancel(ordersDTO.getId() + "");
                            }

                            @Override
                            public void onNegativeClick(View v) {

                            }
                        }).show();
                    }

                    @Override
                    public void pay(OrderListBean.OrdersDTO ordersDTO, int pos) {
                        Intent intent = new Intent(getActivity(), PayActivity.class);
                        intent.putExtra("id", ordersDTO.getId() + "");
                        intent.putExtra("orderNumber", ordersDTO.getOrderNumber());
                        if (ordersDTO.getConfirm() != null) {
                            intent.putExtra("totalPrice", ordersDTO.getConfirm().getTotalPrice());
                            intent.putExtra("totalUseCoupon", ordersDTO.getConfirm().getTotalUseCoupon());
                            intent.putExtra("needCash", ordersDTO.getConfirm().isNeedCash());
                        }
                        intent.putExtra("source", "orderList");
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void queRenShouHuo(String id, int pos) {
                        DialogUtil.build2BtnDialog(getActivity(), "请收到商品确认无误后再确认收货", "确认收货", "取消", true, new DialogUtil.DialogListener2Btn() {
                            @Override
                            public void onPositiveClick(View v) {
                                orderTakeover(id);
                            }

                            @Override
                            public void onNegativeClick(View v) {

                            }
                        }).show();
                    }

                    @Override
                    public void refresh(OrderListBean.OrdersDTO ordersDTO, int pos) {
                        if (isVisibleToUser) {
                            OrderTabFragment.this.refresh();
                        }
                    }
                });
                mFlexibleAdapter.addItem(orderFlexibleItem);
            }
        }
    }

    /**
     * 查看全部订单
     */
    private void orderList(boolean load) {
        if (load) {
            showLoading();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        if (!TextUtils.isEmpty(type)) {
            paramsMap.put("orderStatus", type);
        }
        paramsMap.put("pageIndex", page + "");
        paramsMap.put("pageSize", size + "");
        OkHttpManager.getInstance().getRequest(Api.ORDER_LIST, paramsMap, new BaseCallBack<BaseResult<OrderListBean>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (load) {
                    cancelLoading();
                }
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<OrderListBean> result) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (page == 1) {
                    mFlexibleAdapter.clear();
                }
                if (result != null && result.getData() != null && result.getData().getOrders() != null && result.getData().getOrders().size() > 0) {
                    page++;
                    updateDataUI(result.getData().getOrders());
                } else {
                    if (mFlexibleAdapter.getItemCount() < 1) {
                        mFlexibleAdapter.addItem(new NoDataFlexibleItem(getActivity()));
                    } else {
                        mFlexibleAdapter.addItem(new BottomFlexibleItem(getActivity()));
                    }
                    binding.smartRefreshLayout.setEnableLoadMore(false);
                    binding.smartRefreshLayout.setNoMoreData(true);
                }
                if (load) {
                    cancelLoading();
                }
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                if (binding != null && binding.smartRefreshLayout != null) {
                    binding.smartRefreshLayout.finishRefresh();
                    binding.smartRefreshLayout.finishLoadMore();
                }
                if (load) {
                    cancelLoading();
                }
            }
        });
    }

    /**
     * 删除订单
     *
     * @param id
     */
    private void orderDelete(String id) {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().postRequest(Api.ORDER_DELETE + "?id=" + id, paramsMap, new BaseCallBack<BaseResult<Object>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<Object> result) {
                cancelLoading();
                refresh();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    /**
     * 取消订单
     *
     * @param id
     */
    private void orderCancel(String id) {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().postRequest(Api.ORDER_CANCEL + "?id=" + id, paramsMap, new BaseCallBack<BaseResult<Object>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();
            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<Object> result) {
                cancelLoading();
                refresh();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
                refresh();
            }
        });
    }

    /**
     * 确定收货
     *
     * @param id
     */
    private void orderTakeover(String id) {
        showLoading();
        HashMap<String, String> paramsMap = new HashMap<>();
        OkHttpManager.getInstance().postRequest(Api.ORDER_TAKEOVER + "?id=" + id, paramsMap, new BaseCallBack<BaseResult<Object>>() {
            @Override
            public void onFailure(Call call, IOException e) {
                cancelLoading();

            }

            @Override
            public void onSuccess(Call call, Response response, BaseResult<Object> result) {
                cancelLoading();
                refresh();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

    /**
     * 添加购物车
     */
    private void addShopcartAction(OrderListBean.OrdersDTO ordersDTO) {
        showLoading();
        List<String> list = new ArrayList<>();
        List<ShopcartActionPara> strings = new ArrayList<>();
        if (ordersDTO.getProducts() != null && ordersDTO.getProducts().size() > 0) {
            for (OrderListBean.OrdersDTO.ProductsDTO productsDTO : ordersDTO.getProducts()) {
                if (productsDTO != null && productsDTO.getGoodsDto() != null) {
                    list.add(productsDTO.getGoodsDto().getGoodsId() + "");
                    ShopcartActionPara shopcartActionPara = new ShopcartActionPara();
                    shopcartActionPara.setProductId(productsDTO.getGoodsDto().getProductId());
                    shopcartActionPara.setGoodsId(productsDTO.getGoodsDto().getGoodsId());
                    shopcartActionPara.setCartType(productsDTO.getProductType());
                    shopcartActionPara.setActionType(0);
                    shopcartActionPara.setProductNum(productsDTO.getProductNum());
                    strings.add(shopcartActionPara);
                }
            }
        }
        if (ordersDTO.getShippers() != null && ordersDTO.getShippers().size() > 0) {
            for (int i = 0; i < ordersDTO.getShippers().size(); i++) {
                if (ordersDTO.getShippers().get(i) != null
                        && ordersDTO.getShippers().get(i).getGoods() != null
                        && ordersDTO.getShippers().get(i).getGoods().size() > 0) {
                    for (OrderListBean.OrdersDTO.ProductsDTO productsDTO : ordersDTO.getShippers().get(i).getGoods()) {
                        if (productsDTO != null && productsDTO.getGoodsDto() != null) {
                            list.add(productsDTO.getGoodsDto().getGoodsId() + "");
                            ShopcartActionPara shopcartActionPara = new ShopcartActionPara();
                            shopcartActionPara.setProductId(productsDTO.getGoodsDto().getProductId());
                            shopcartActionPara.setGoodsId(productsDTO.getGoodsDto().getGoodsId());
                            shopcartActionPara.setCartType(productsDTO.getProductType());
                            shopcartActionPara.setActionType(0);
                            shopcartActionPara.setProductNum(productsDTO.getProductNum());
                            strings.add(shopcartActionPara);
                        }
                    }
                }
            }
        }
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
                LiveEventBus.get("addZaiCiGouMai", List.class).post(list);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("shopping", "shopping");
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onError(Call call, int statusCode, Exception e) {
                cancelLoading();
            }
        });
    }

}
