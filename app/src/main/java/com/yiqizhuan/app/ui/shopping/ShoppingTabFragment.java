package com.yiqizhuan.app.ui.shopping;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yiqizhuan.app.bean.PaymentConfirmBean;
import com.yiqizhuan.app.bean.ShopCartBean;
import com.yiqizhuan.app.databinding.FragmentShoppingTabBinding;
import com.yiqizhuan.app.ui.base.BaseFragment;
import com.yiqizhuan.app.ui.shopping.item.ShoppingFlexibleItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * @author LiPeng
 * @create 2024-04-17 7:12 PM
 */
public class ShoppingTabFragment extends BaseFragment {

    private FragmentShoppingTabBinding binding;
    private FlexibleAdapter<IFlexible> mFlexibleAdapter;
    private String type;
    private ShopCartListenerSure shopCartListenerSure;

    public void setShopCartListenerSure(ShopCartListenerSure shopCartListenerSure) {
        this.shopCartListenerSure = shopCartListenerSure;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentShoppingTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
        initView();
        return root;
    }

    private void initView() {
        mFlexibleAdapter = new FlexibleAdapter<>(null);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(mFlexibleAdapter);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void initData(List<ShopCartBean.DetailsDTO> details) {
        mFlexibleAdapter.clear();
        if (details != null && details.size() > 0) {
            for (ShopCartBean.DetailsDTO detailsDTO : details) {
                if ("0".equals(type)) {
                    addData(detailsDTO);
                } else if ("1".equals(type) && TextUtils.equals(String.valueOf(detailsDTO.getCartType()), type)) {
                    addData(detailsDTO);
                } else if ("2".equals(type) && TextUtils.equals(String.valueOf(detailsDTO.getCartType()), type)) {
                    addData(detailsDTO);
                } else if ("3".equals(type) && TextUtils.equals(String.valueOf(detailsDTO.getCartType()), type)) {
                    addData(detailsDTO);
                } else if ("4".equals(type) && TextUtils.equals(String.valueOf(detailsDTO.getCartType()), type)) {
                    addData(detailsDTO);
                }
            }
        }
    }

    private void addData(ShopCartBean.DetailsDTO detailsDTO) {
        ShoppingFlexibleItem shoppingFlexibleItem = new ShoppingFlexibleItem(getActivity(), detailsDTO);
        shoppingFlexibleItem.setShopCartListenerSure(new ShoppingFlexibleItem.ShopCartListenerSure() {
            @Override
            public void add(ShopCartBean.DetailsDTO detailsDTO, int num) {
                if (shopCartListenerSure != null) {
                    shopCartListenerSure.add(detailsDTO, num);
                }
            }

            @Override
            public void subtraction(ShopCartBean.DetailsDTO detailsDTO, int num) {
                if (shopCartListenerSure != null) {
                    shopCartListenerSure.subtraction(detailsDTO, num);
                }
            }

            @Override
            public void select() {
                if (shopCartListenerSure != null) {
                    shopCartListenerSure.select();
                }
            }
        });
        mFlexibleAdapter.addItem(shoppingFlexibleItem);
    }

    public void refresh() {
        mFlexibleAdapter.notifyDataSetChanged();
    }

    public void refreshItem(int goodsId) {
        for (int i = 0; i < mFlexibleAdapter.getItemCount(); i++) {
            ShoppingFlexibleItem shoppingFlexibleItem = (ShoppingFlexibleItem) mFlexibleAdapter.getItem(i);
            if (goodsId == shoppingFlexibleItem.getDetailsDTO().getGoodsVO().getGoodsId()) {
                mFlexibleAdapter.notifyItemChanged(i);
            }
        }
    }

    public void refreshMultiItem(List<PaymentConfirmBean.ProductsDTO> products) {
        for (int i = 0; i < mFlexibleAdapter.getItemCount(); i++) {
            ShoppingFlexibleItem shoppingFlexibleItem = (ShoppingFlexibleItem) mFlexibleAdapter.getItem(i);
            for (PaymentConfirmBean.ProductsDTO productsDTO : products) {
                if (productsDTO.getGoodsVO().getGoodsId() == shoppingFlexibleItem.getDetailsDTO().getGoodsVO().getGoodsId()) {
                    mFlexibleAdapter.notifyItemChanged(i);
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface ShopCartListenerSure {
        void add(ShopCartBean.DetailsDTO detailsDTO, int num);

        void subtraction(ShopCartBean.DetailsDTO detailsDTO, int num);

        void select();
    }

}
