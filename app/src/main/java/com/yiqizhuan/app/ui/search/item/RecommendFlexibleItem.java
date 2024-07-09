package com.yiqizhuan.app.ui.search.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;
import com.yiqizhuan.app.bean.ProductListBean;
import com.yiqizhuan.app.bean.ProductSearchDefaultBean;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * 搜索推荐
 */
public class RecommendFlexibleItem extends AbstractFlexibleItem<RecommendFlexibleItem.ItemViewHolder> {
    Context context;
    ProductSearchDefaultBean productSearchDefaultBean;


    public RecommendFlexibleItem(Context context, ProductSearchDefaultBean productSearchDefaultBean) {
        this.context = context;
        this.productSearchDefaultBean = productSearchDefaultBean;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_search_recommend;
    }

    @Override
    public ItemViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ItemViewHolder holder, int position, List<Object> payloads) {
        holder.tvName.setText(productSearchDefaultBean.getTitle());
        FlexibleAdapter<IFlexible> recommendFlexibleAdapter = new FlexibleAdapter<>(null);
        holder.rcRecommend.setLayoutManager(new LinearLayoutManager(context));
        holder.rcRecommend.setAdapter(recommendFlexibleAdapter);
        holder.rcRecommend.setItemAnimator(new DefaultItemAnimator());
        List<ProductListBean.Detail> details = productSearchDefaultBean.getProducts();
        if (position == 0) {
            if (details != null && details.size() > 1) {
                recommendFlexibleAdapter.addItem(new RecommendFirstFlexibleItem(context, details.get(0), details.get(1)));
            }
            if (details != null && details.size() > 2) {
                for (int i = 0; i < details.size(); i++) {
                    if (i > 1 && i < 10) {
                        recommendFlexibleAdapter.addItem(new RecommendCommonFlexibleItem(context, details.get(i)));
                    }
                }
            }
        } else {
            if (details != null && details.size() > 0) {
                for (int i = 0; i < details.size(); i++) {
                    if (i < 10) {
                        recommendFlexibleAdapter.addItem(new RecommendCommonFlexibleItem(context, details.get(i)));
                    }
                }
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RecyclerView rcRecommend;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            rcRecommend = itemView.findViewById(R.id.rcRecommend);
        }

    }

}
