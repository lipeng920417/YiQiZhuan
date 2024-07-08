package com.yiqizhuan.app.ui.search.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiqizhuan.app.R;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * 搜索推荐
 */
public class RecommendFlexibleItem extends AbstractFlexibleItem<RecommendFlexibleItem.ItemViewHolder> {
    Context context;

    public RecommendFlexibleItem(Context context) {
        this.context = context;
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
        FlexibleAdapter<IFlexible> recommendFlexibleAdapter = new FlexibleAdapter<>(null);
        holder.rcRecommend.setLayoutManager(new LinearLayoutManager(context));
        holder.rcRecommend.setAdapter(recommendFlexibleAdapter);
        holder.rcRecommend.setItemAnimator(new DefaultItemAnimator());
        for (int i = 0; i < 10; i++) {
            recommendFlexibleAdapter.addItem(new RecommendFlexibleItem(this));
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
